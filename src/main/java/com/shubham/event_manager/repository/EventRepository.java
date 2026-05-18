package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByLocation(String location);
    List<Event> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT e FROM Event e " +
            "WHERE e.status = 'PUBLISHED' " +
            "ORDER BY e.eventDate ASC")
    List<Event> findAllPublished();

    @Query("SELECT e FROM Event e " +
            "WHERE e.status = 'PUBLISHED' " +
            "AND e.eventDate > :now " +
            "ORDER BY e.eventDate ASC")
    List<Event> findUpcoming(
            @Param("now") LocalDateTime now);

    @Query("SELECT e FROM Event e " +
            "WHERE e.status = 'PUBLISHED' " +
            "AND e.eventDate < :now " +
            "ORDER BY e.eventDate DESC")
    List<Event> findPast(
            @Param("now") LocalDateTime now);

}
