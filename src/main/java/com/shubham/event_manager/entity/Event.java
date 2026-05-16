package com.shubham.event_manager.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    private String location;

    private Integer capacity;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_categories",
            joinColumns = @JoinColumn(
                    name = "event_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id")
    )
    private List<Category> categories
            = new ArrayList<>();

    @Column(nullable = false)
    private Integer registrationsCount = 0;

    @OneToMany(
            mappedBy = "event",
            fetch = FetchType.LAZY
    )
    private List<Registration> registrations
            = new ArrayList<>();

    public boolean isFull() {
        if (capacity == null) return false;
        return registrationsCount >= capacity;
    }

    public int getSpotsRemaining() {
        if (capacity == null) return Integer.MAX_VALUE;
        return Math.max(0, capacity - registrationsCount);
    }


}
