package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationFollowerRepository
        extends JpaRepository<OrganizationFollower,
        OrganizationFollowerId> {

    boolean existsByUserAndOrganization(
            User user, Organization organization);

    Optional<OrganizationFollower> findByUserAndOrganization(
            User user, Organization organization);

    long countByOrganization(Organization organization);
}