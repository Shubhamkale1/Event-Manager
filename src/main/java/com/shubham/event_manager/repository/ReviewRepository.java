package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.entity.Review;
import com.shubham.event_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByEventOrderByCreatedAtDesc(
            Event event);

    List<Review> findByUserOrderByCreatedAtDesc(
            User user);

    Optional<Review> findByUserAndEvent(
            User user, Event event);

    boolean existsByUserAndEvent(
            User user, Event event);

    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.event = :event
        """)
    Double findAverageRatingByEvent(
            @Param("event") Event event);

    long countByEvent(Event event);
}