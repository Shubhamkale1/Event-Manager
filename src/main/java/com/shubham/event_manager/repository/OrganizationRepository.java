package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Organization;
import com.shubham.event_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {

    List<Organization> findByOwner(User owner);

    boolean existsByNameIgnoreCase(String name);

    @Query("""
        SELECT o FROM Organization o
        JOIN o.followers f
        WHERE f.user = :user
        ORDER BY f.followedAt DESC
        """)
    List<Organization> findOrganizationsFollowedByUser(
            @Param("user") User user);

    @Query("""
        SELECT COUNT(f) FROM OrganizationFollower f
        WHERE f.organization.id = :orgId
        """)
    long countFollowers(@Param("orgId") Long orgId);
}