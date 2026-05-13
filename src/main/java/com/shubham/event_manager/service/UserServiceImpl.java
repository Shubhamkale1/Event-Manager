package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.ChangePasswordRequest;
import com.shubham.event_manager.dto.UserProfileDTO;
import com.shubham.event_manager.entity.User;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    private UserProfileDTO toDTO(User user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .phone(user.getPhone())
                .city(user.getCity())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getMyProfile(String email) {
        return toDTO(getUser(email));
    }

    @Override
    @Transactional
    public UserProfileDTO updateMyProfile(
            String email, UserProfileDTO dto) {

        User user = getUser(email);

        // Only update allowed fields
        // Email and role are never updated here
        if (dto.getName() != null
                && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        user.setBio(dto.getBio());
        user.setPhone(dto.getPhone());
        user.setCity(dto.getCity());

        User saved = userRepository.save(user);
        log.info("Profile updated for: {}", email);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void changePassword(
            String email,
            ChangePasswordRequest request) {

        User user = getUser(email);

        // Guard 1 — verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {
            throw new IllegalArgumentException(
                    "Current password is incorrect");
        }

        // Guard 2 — new passwords must match
        if (!request.getNewPassword().equals(
                request.getConfirmPassword())) {
            throw new IllegalArgumentException(
                    "New passwords do not match");
        }

        // Guard 3 — new password different from current
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {
            throw new IllegalArgumentException(
                    "New password must be different " +
                            "from current password");
        }

        user.setPassword(passwordEncoder.encode(
                request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for: {}", email);
    }

    @Override
    @Transactional
    public void deleteMyAccount(String email) {
        User user = getUser(email);
        userRepository.delete(user);
        log.info("Account deleted for: {}", email);
    }
}