package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.entity.User;
import com.shubham.event_manager.entity.WaitlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitlistRepository
        extends JpaRepository<WaitlistEntry, Long> {

    boolean existsByUserAndEvent(
            User user, Event event);

    Optional<WaitlistEntry> findByUserAndEvent(
            User user, Event event);

    List<WaitlistEntry>
    findByEventAndStatusOrderByPositionAsc(
            Event event, String status);

    long countByEventAndStatus(
            Event event, String status);

    @Query("""
        SELECT MAX(w.position)
        FROM WaitlistEntry w
        WHERE w.event = :event
        """)
    Integer findMaxPosition(
            @Param("event") Event event);

    Optional<WaitlistEntry>
    findFirstByEventAndStatusOrderByPositionAsc(
            Event event, String status);
}