//package com.shubham.event_manager.service;
//
//
//import com.shubham.event_manager.dto.EventDTO;
//import com.shubham.event_manager.entity.Event;
//import com.shubham.event_manager.exception.ResourceNotFoundException;
//import com.shubham.event_manager.mapper.EventMapper;
//import com.shubham.event_manager.repository.EventRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class EventServiceImplTest {
//
//    @Mock
//    private EventRepository eventRepository;
//
//    @Mock
//    private EventMapper eventMapper;
//
//    @InjectMocks
//    private EventServiceImpl eventService;
//
//    private Event event;
//
//    private EventDTO eventDTO;
//
//    @BeforeEach
//    void setUp(){
//        new Event(1L, "Test", "Desc", LocalDateTime.now(), "Pune", 100, LocalDateTime.now());
//
//        new EventDTO(1L, "Test", "Desc", LocalDateTime.now(), "Pune", 100, LocalDateTime.now());
//    }
//
//    @Test
//    void getAllEvents_shouldReturnListOfDTOs(){
//        when(eventRepository.findAll()).thenReturn(List.of(event));
//        when(eventMapper.toDTO(event)).thenReturn(eventDTO);
//
//        List<EventDTO> result = eventService.getAllEvents();
//
//        assertEquals(1, result.size());
//
//        assertEquals("Spring Hackathon", result.get(0).getTitle());
//        verify(eventRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getEventById_whenExists_shouldReturnDTO() {
//        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
//        when(eventMapper.toDTO(event)).thenReturn(eventDTO);
//
//        EventDTO result = eventService.getEventById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//    }
//
//    @Test
//    void getEventById_whenNotExists_shouldThrowException() {
//        when(eventRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class,
//                () -> eventService.getEventById(99L));
//    }
//
//    @Test
//    void createEvents_shouldSaveAndReturnDTO(){
//        when(eventMapper.toEntity(eventDTO)).thenReturn(event);
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//        when(eventMapper.toDTO(event)).thenReturn(eventDTO);
//
//        EventDTO result = eventService.createEvent(eventDTO, "test-user");
//
//        assertNotNull(result);
//        verify(eventRepository, times(1)).save(any(Event.class));
//    }
//
//    @Test
//    void deleteEvent_whenNotExists_shouldThrowException() {
//        when(eventRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class,
//                () -> eventService.deleteEvent(99L));
//
//        verify(eventRepository,never()).delete(any());
//    }
//}
