package com.shubham.event_manager.service.impl;

import com.shubham.event_manager.dto.OrganizationDTO;
import com.shubham.event_manager.dto.OrganizationSummaryDTO;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.NotificationService;
import com.shubham.event_manager.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl
        implements OrganizationService {

    private final OrganizationRepository orgRepository;
    private final OrganizationFollowerRepository
            followerRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // ── Helper ─────────────────────────────────────────

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    private Organization getOrg(Long id) {
        return orgRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization not found: " + id));
    }

    private OrganizationDTO toDTO(
            Organization org, String currentUserEmail) {

        long followerCount =
                followerRepository.countByOrganization(org);

        boolean isFollowing = false;
        if (currentUserEmail != null) {
            userRepository.findByEmail(currentUserEmail)
                    .ifPresent(user -> {
                        // note: reassignment not possible
                        // in lambda — handled below
                    });
            User user = userRepository
                    .findByEmail(currentUserEmail)
                    .orElse(null);
            if (user != null) {
                isFollowing = followerRepository
                        .existsByUserAndOrganization(
                                user, org);
            }
        }

        return OrganizationDTO.builder()
                .id(org.getId())
                .name(org.getName())
                .description(org.getDescription())
                .website(org.getWebsite())
                .location(org.getLocation())
                .ownerId(org.getOwner().getId())
                .ownerName(org.getOwner().getName())
                .followerCount((int) followerCount)
                .isFollowing(isFollowing)
                .createdAt(org.getCreatedAt())
                .build();
    }

    private OrganizationSummaryDTO toSummaryDTO(
            Organization org) {
        return new OrganizationSummaryDTO(
                org.getId(),
                org.getName(),
                org.getLocation(),
                (int) followerRepository
                        .countByOrganization(org)
        );
    }

    // ── Service Methods ────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationSummaryDTO> getAllOrganizations() {
        return orgRepository.findAll()
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationDTO getOrganizationById(Long id) {
        return toDTO(getOrg(id), null);
    }

    @Override
    @Transactional
    public OrganizationDTO createOrganization(
            OrganizationDTO dto, String ownerEmail) {

        // Guard clause 1 — duplicate name check
        if (orgRepository.existsByNameIgnoreCase(
                dto.getName())) {
            throw new IllegalArgumentException(
                    "Organization already exists: "
                            + dto.getName());
        }

        User owner = getUser(ownerEmail);

        Organization org = Organization.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .website(dto.getWebsite())
                .location(dto.getLocation())
                .owner(owner)
                .build();

        Organization saved = orgRepository.save(org);
        log.info("Organization created: {} by {}",
                saved.getName(), ownerEmail);

        return toDTO(saved, ownerEmail);
    }

    @Override
    @Transactional
    public OrganizationDTO updateOrganization(
            Long id, OrganizationDTO dto,
            String currentUserEmail) {

        Organization org = getOrg(id);
        User currentUser = getUser(currentUserEmail);

        // Guard clause — ownership check
        if (!org.getOwner().getId()
                .equals(currentUser.getId())) {
            throw new AccessDeniedException(
                    "Only the owner can update this organization");
        }

        org.setName(dto.getName());
        org.setDescription(dto.getDescription());
        org.setWebsite(dto.getWebsite());
        org.setLocation(dto.getLocation());

        return toDTO(orgRepository.save(org),
                currentUserEmail);
    }

    @Override
    @Transactional
    public void deleteOrganization(
            Long id, String currentUserEmail) {

        Organization org = getOrg(id);
        User currentUser = getUser(currentUserEmail);

        // Guard clause — only owner or admin can delete
        boolean isOwner = org.getOwner().getId()
                .equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole()
                .equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException(
                    "Only the owner or admin can delete " +
                            "this organization");
        }

        orgRepository.delete(org);
        log.info("Organization deleted: {} by {}",
                id, currentUserEmail);
    }

    @Override
    @Transactional
    public void followOrganization(
            Long orgId, String userEmail) {

        User user = getUser(userEmail);
        Organization org = getOrg(orgId);

        // Guard clause — already following
        if (followerRepository.existsByUserAndOrganization(
                user, org)) {
            throw new IllegalArgumentException(
                    "Already following this organization");
        }

        // Guard clause — cannot follow own organization
        if (org.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Cannot follow your own organization");
        }

        OrganizationFollower follower =
                OrganizationFollower.builder()
                        .id(new OrganizationFollowerId(
                                user.getId(), org.getId()))
                        .user(user)
                        .organization(org)
                        .build();

        followerRepository.save(follower);
        notificationService.notifyNewFollower(org, user);
        log.info("{} followed {}", userEmail, org.getName());
    }

    @Override
    @Transactional
    public void unfollowOrganization(
            Long orgId, String userEmail) {

        User user = getUser(userEmail);
        Organization org = getOrg(orgId);

        OrganizationFollower follower =
                followerRepository.findByUserAndOrganization(
                                user, org)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Not following this organization"));

        followerRepository.delete(follower);
        log.info("{} unfollowed {}",
                userEmail, org.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationSummaryDTO> getMyOrganizations(
            String userEmail) {
        User user = getUser(userEmail);
        return orgRepository.findByOwner(user)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationSummaryDTO>
    getFollowedOrganizations(String userEmail) {
        User user = getUser(userEmail);
        return orgRepository
                .findOrganizationsFollowedByUser(user)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }
}