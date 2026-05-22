package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    Optional<Registration> findByUserAndEvent(
            User user, Event event);

    boolean existsByUserAndEvent(
            User user, Event event);

    List<Registration> findByUserOrderByRegisteredAtDesc(
            User user);

    List<Registration> findByEventOrderByRegisteredAtAsc(
            Event event);

    long countByEventAndStatus(
            Event event, RegistrationStatus status);

    // Atomic increment — the key to concurrency safety
    @Modifying
    @Query("""
        UPDATE Event e
        SET e.registrationsCount =
            e.registrationsCount + 1
        WHERE e.id = :eventId
        AND e.registrationsCount < e.capacity
        """)
    int incrementRegistrationCount(
            @Param("eventId") Long eventId);

    // Atomic decrement on cancellation
    @Modifying
    @Query("""
        UPDATE Event e
        SET e.registrationsCount =
            e.registrationsCount - 1
        WHERE e.id = :eventId
        AND e.registrationsCount > 0
        """)
    int decrementRegistrationCount(
            @Param("eventId") Long eventId);
}