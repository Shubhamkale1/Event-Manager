package com.shubham.event_manager.mapper;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.Event;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T23:07:12+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDTO toDTO(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();

        eventDTO.setId( event.getId() );
        eventDTO.setTitle( event.getTitle() );
        eventDTO.setDescription( event.getDescription() );
        eventDTO.setEventDate( event.getEventDate() );
        eventDTO.setLocation( event.getLocation() );
        eventDTO.setCapacity( event.getCapacity() );
        eventDTO.setCreatedAt( event.getCreatedAt() );

        return eventDTO;
    }

    @Override
    public Event toEntity(EventDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Event event = new Event();

        event.setId( dto.getId() );
        event.setTitle( dto.getTitle() );
        event.setDescription( dto.getDescription() );
        event.setEventDate( dto.getEventDate() );
        event.setLocation( dto.getLocation() );
        event.setCapacity( dto.getCapacity() );
        event.setCreatedAt( dto.getCreatedAt() );

        return event;
    }

    @Override
    public void updateEntityFromDTO(EventDTO dto, Event event) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            event.setId( dto.getId() );
        }
        if ( dto.getTitle() != null ) {
            event.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            event.setDescription( dto.getDescription() );
        }
        if ( dto.getEventDate() != null ) {
            event.setEventDate( dto.getEventDate() );
        }
        if ( dto.getLocation() != null ) {
            event.setLocation( dto.getLocation() );
        }
        if ( dto.getCapacity() != null ) {
            event.setCapacity( dto.getCapacity() );
        }
        if ( dto.getCreatedAt() != null ) {
            event.setCreatedAt( dto.getCreatedAt() );
        }
    }
}
