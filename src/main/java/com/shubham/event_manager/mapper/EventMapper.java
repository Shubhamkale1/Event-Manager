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

    @Mapping(target = "venueId", source = "venue.id")
    @Mapping(target = "venue", source = "venue")
    @Mapping(target = "categoryIds", expression = "java(mapCategoryIds(event))")
    @Mapping(target = "categoryNames", expression = "java(mapCategoryNames(event))")
    EventDTO toDTO(Event event);

    Event toEntity(EventDTO dto);

    // 🔄 update existing entity
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntityFromDTO(
            EventDTO dto,
            @MappingTarget Event event
    );

    // =========================
    // Helper methods
    // =========================

    default List<Long> mapCategoryIds(Event event) {
        if (event.getCategories() == null) {
            return null;
        }

        return event.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    default List<String> mapCategoryNames(Event event) {
        if (event.getCategories() == null) {
            return null;
        }

        return event.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}