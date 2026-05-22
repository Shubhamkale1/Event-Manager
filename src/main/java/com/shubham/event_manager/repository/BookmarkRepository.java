package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository
        extends JpaRepository<Event, Long> {

    @Query("""
        SELECT e FROM Event e
        JOIN e.bookmarkedBy u
        WHERE u = :user
        ORDER BY e.eventDate ASC
        """)
    List<Event> findBookmarkedByUser(
            @Param("user") User user);

    @Query("""
        SELECT COUNT(u) > 0 FROM Event e
        JOIN e.bookmarkedBy u
        WHERE e = :event AND u = :user
        """)
    boolean isBookmarkedByUser(
            @Param("event") Event event,
            @Param("user") User user);
}