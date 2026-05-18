package com.shubham.event_manager.mapper;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.Category;
import com.shubham.event_manager.entity.Event;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {VenueMapper.class}
)
public interface EventMapper {

    // ── toDTO ──────────────────────────────────────────
    @Mapping(
            target = "venueId",
            expression = "java(event.getVenue() != null " +
                    "? event.getVenue().getId() : null)"
    )
    @Mapping(
            target = "venue",
            source = "venue"
    )
    @Mapping(
            target = "categoryIds",
            expression = "java(mapCategoryIds(event))"
    )
    @Mapping(
            target = "categoryNames",
            expression = "java(mapCategoryNames(event))"
    )
    @Mapping(
            target = "registrationsCount",
            expression = "java(event.getRegistrationsCount())"
    )
    @Mapping(
            target = "spotsRemaining",
            expression = "java(event.getCapacity() != null " +
                    "? event.getSpotsRemaining() : null)"
    )
    @Mapping(
            target = "isFull",
            expression = "java(event.getCapacity() != null " +
                    "? event.isFull() : false)"
    )
    @Mapping(target = "status", source = "status")
    @Mapping(target = "publishedAt", source = "publishedAt")
    @Mapping(target = "cancelledAt", source = "cancelledAt")
    @Mapping(target = "completedAt", source = "completedAt")
    @Mapping(target = "cancellationReason",
            source = "cancellationReason")
    EventDTO toDTO(Event event);

    // ── toEntity ───────────────────────────────────────
    // These fields cannot be auto-mapped from DTO
    // They are handled manually in the service layer
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "registrationsCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Event toEntity(EventDTO dto);

    // ── updateEntityFromDTO ────────────────────────────
    // Ignore all system-managed and relationship fields
    // Only basic fields like title, description, eventDate,
    // location, capacity are updated from DTO
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "registrationsCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(
            EventDTO dto,
            @MappingTarget Event event
    );

    // ── Helper methods ─────────────────────────────────

    default List<Long> mapCategoryIds(Event event) {
        if (event.getCategories() == null
                || event.getCategories().isEmpty()) {
            return null;
        }
        return event.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    default List<String> mapCategoryNames(Event event) {
        if (event.getCategories() == null
                || event.getCategories().isEmpty()) {
            return null;
        }
        return event.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }


}