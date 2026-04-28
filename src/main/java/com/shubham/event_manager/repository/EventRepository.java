package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByLocation(String location);
    List<Event> findByTitleContainingIgnoreCase(String title);
}
