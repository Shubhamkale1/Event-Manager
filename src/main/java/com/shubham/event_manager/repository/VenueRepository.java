package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository
        extends JpaRepository<Venue, Long> {

    List<Venue> findByCityIgnoreCase(String city);

    List<Venue> findByCapacityGreaterThanEqual(
            Integer minCapacity);

    boolean existsByNameAndCity(String name, String city);
}
