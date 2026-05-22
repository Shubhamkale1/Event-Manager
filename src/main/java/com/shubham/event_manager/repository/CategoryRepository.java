package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("""
        SELECT c FROM Category c
        LEFT JOIN c.events e
        GROUP BY c
        ORDER BY COUNT(e) DESC
        """)
    List<Category> findAllOrderByEventCountDesc();
}