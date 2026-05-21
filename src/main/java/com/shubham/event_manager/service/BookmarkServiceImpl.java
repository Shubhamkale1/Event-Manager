package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.mapper.EventMapper;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl
        implements BookmarkService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final EventMapper eventMapper;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found: " + eventId));
    }

    @Override
    @Transactional
    public void addBookmark(
            Long eventId, String userEmail) {
        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        if (bookmarkRepository.isBookmarkedByUser(
                event, user)) {
            throw new IllegalArgumentException(
                    "Event already bookmarked");
        }

        event.getBookmarkedBy().add(user);
        eventRepository.save(event);
        log.info("{} bookmarked event {}",
                userEmail, eventId);
    }

    @Override
    @Transactional
    public void removeBookmark(
            Long eventId, String userEmail) {
        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        if (!bookmarkRepository.isBookmarkedByUser(
                event, user)) {
            throw new IllegalArgumentException(
                    "Event not bookmarked");
        }

        event.getBookmarkedBy().remove(user);
        eventRepository.save(event);
        log.info("{} removed bookmark for event {}",
                userEmail, eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDTO> getMyBookmarks(
            String userEmail) {
        User user = getUser(userEmail);
        return bookmarkRepository
                .findBookmarkedByUser(user)
                .stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(
            Long eventId, String userEmail) {
        User user = getUser(userEmail);
        Event event = getEvent(eventId);
        return bookmarkRepository
                .isBookmarkedByUser(event, user);
    }
}