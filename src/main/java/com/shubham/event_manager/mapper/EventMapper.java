package com.shubham.event_manager.mapper;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDTO toDTO(Event event);


    Event toEntity(EventDTO dto);

    // 🔄 update existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(EventDTO dto, @MappingTarget Event event);


}