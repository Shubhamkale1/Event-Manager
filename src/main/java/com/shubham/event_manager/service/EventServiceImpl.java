package com.shubham.event_manager.service;


import com.shubham.event_manager.document.EventDocument;
import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.entity.Venue;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.mapper.EventMapper;
import com.shubham.event_manager.repository.EventRepository;
import com.shubham.event_manager.repository.EventSearchRepository;
import com.shubham.event_manager.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository       eventRepository;
    private final EventSearchRepository eventSearchRepository;
    private final EventMapper           eventMapper;
    private final EmailService          emailService;
    private final VenueRepository       venueRepository;

    @Override
    @Cacheable(value = "events")
    public List<EventDTO> getAllEvents() {
        log.info("Cache miss — fetching all events from database");
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "event", key = "#id")
    public EventDTO getEventById(Long id) {
        log.info("Cache miss — fetching event {} from database", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id: " + id));
        return eventMapper.toDTO(event);
    }

    @Override
    @CacheEvict(value = {"events", "event"}, allEntries = true)
    public EventDTO createEvent(EventDTO eventDTO, String userEmail) {
        Event event = eventMapper.toEntity(eventDTO);
        if (eventDTO.getVenueId() != null) {
            Venue venue = venueRepository.findById(
                            eventDTO.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Venue not found: " + eventDTO.getVenueId()));

            // Check venue capacity vs event capacity
            if (venue.getCapacity() != null
                    && eventDTO.getCapacity() != null
                    && eventDTO.getCapacity() > venue.getCapacity()) {
                throw new IllegalArgumentException(
                        "Event capacity " + eventDTO.getCapacity()
                                + " exceeds venue capacity "
                                + venue.getCapacity());
            }
            event.setVenue(venue);
        }
        Event saved = eventRepository.save(event);

        EventDocument document = mapToDocument(saved);
        eventSearchRepository.save(document);
        log.info("Event indexed in Elasticsearch: {}", saved.getId());

        EventDTO result = eventMapper.toDTO(saved);
        emailService.sendEventConfirmation(result, userEmail);

        return result;
    }

    @Override
    @CacheEvict(value = {"events", "event"}, allEntries = true)
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id: " + id));
        eventMapper.updateEntityFromDTO(eventDTO, existing);
        if (eventDTO.getVenueId() != null) {
            Venue venue = venueRepository.findById(
                            eventDTO.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Venue not found: " + eventDTO.getVenueId()));

            if (venue.getCapacity() != null
                    && eventDTO.getCapacity() != null
                    && eventDTO.getCapacity() > venue.getCapacity()) {
                throw new IllegalArgumentException(
                        "Event capacity " + eventDTO.getCapacity()
                                + " exceeds venue capacity "
                                + venue.getCapacity());
            }

            existing.setVenue(venue);
        }
        Event saved = eventRepository.save(existing);

        eventSearchRepository.save(mapToDocument(saved));

        return eventMapper.toDTO(saved);
    }

    @Override
    @CacheEvict(value = {"events", "event"}, allEntries = true)
    public void deleteEvent(Long id) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id: " + id));
        eventRepository.delete(existing);
        eventSearchRepository.deleteById(String.valueOf(id));
        log.info("Event {} deleted from MySQL and Elasticsearch", id);
    }

    @Override
    public List<EventDocument> searchEvents(String query) {
        log.info("Searching Elasticsearch for: {}", query);
        return eventSearchRepository.fuzzySearch(query);
    }

    private EventDocument mapToDocument(Event event) {
        return new EventDocument(
                String.valueOf(event.getId()),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getEventDate(),
                event.getCapacity()
        );
    }

}
