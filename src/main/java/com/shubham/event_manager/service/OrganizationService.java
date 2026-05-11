package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.OrganizationDTO;
import com.shubham.event_manager.dto.OrganizationSummaryDTO;

import java.util.List;

public interface OrganizationService {

    List<OrganizationSummaryDTO> getAllOrganizations();

    OrganizationDTO getOrganizationById(Long id);

    OrganizationDTO createOrganization(
            OrganizationDTO dto, String ownerEmail);

    OrganizationDTO updateOrganization(
            Long id, OrganizationDTO dto, String currentUserEmail);

    void deleteOrganization(
            Long id, String currentUserEmail);

    void followOrganization(
            Long orgId, String userEmail);

    void unfollowOrganization(
            Long orgId, String userEmail);

    List<OrganizationSummaryDTO> getMyOrganizations(
            String userEmail);

    List<OrganizationSummaryDTO> getFollowedOrganizations(
            String userEmail);
}