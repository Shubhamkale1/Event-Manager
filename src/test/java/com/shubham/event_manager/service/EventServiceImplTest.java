package com.shubham.event_manager.service;


import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.entity.EventStatus;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.mapper.EventMapper;
import com.shubham.event_manager.repository.CategoryRepository;
import com.shubham.event_manager.repository.EventRepository;
import com.shubham.event_manager.repository.EventSearchRepository;
import com.shubham.event_manager.repository.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EventSearchRepository eventSearchRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setTitle("Test");
        event.setDescription("Desc");
        event.setLocation("Pune");
        event.setCapacity(100);
        event.setEventDate(LocalDateTime.now());
        event.setRegistrationsCount(0);
        event.setStatus(EventStatus.PUBLISHED);

        eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setTitle("Test");
        eventDTO.setDescription("Desc");
        eventDTO.setLocation("Pune");
        eventDTO.setCapacity(100);
        eventDTO.setEventDate(LocalDateTime.now());
    }

    @Test
    void getAllEvents_shouldReturnListOfDTOs() {
        when(eventRepository.findAllPublished())
                .thenReturn(List.of(event));
        when(eventMapper.toDTO(event))
                .thenReturn(eventDTO);

        List<EventDTO> result = eventService.getAllEvents();

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getTitle());
        verify(eventRepository, times(1))
                .findAllPublished();
    }

    @Test
    void getEventById_whenExists_shouldReturnDTO() {
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));
        when(eventMapper.toDTO(event))
                .thenReturn(eventDTO);

        EventDTO result = eventService.getEventById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getEventById_whenNotExists_shouldThrowException() {
        when(eventRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.getEventById(99L));
    }

    @Test
    void createEvent_shouldSaveAndReturnDTO() {
        when(eventMapper.toEntity(eventDTO))
                .thenReturn(event);
        when(eventRepository.save(any(Event.class)))
                .thenReturn(event);
        when(eventMapper.toDTO(event))
                .thenReturn(eventDTO);

        // Mock email to do nothing (async)
        doNothing().when(emailService)
                .sendEventConfirmation(any(), any());

        EventDTO result = eventService.createEvent(
                eventDTO, "test@example.com");

        assertNotNull(result);
        verify(eventRepository, times(1))
                .save(any(Event.class));
    }

    @Test
    void deleteEvent_whenNotExists_shouldThrowException() {
        when(eventRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.deleteEvent(99L));

        verify(eventRepository, never())
                .delete(any());
    }
}
