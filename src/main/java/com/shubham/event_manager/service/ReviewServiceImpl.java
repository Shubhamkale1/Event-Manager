package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.ReviewDTO;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl
        implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository
            registrationRepository;

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

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .eventId(review.getEvent().getId())
                .eventTitle(review.getEvent().getTitle())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public ReviewDTO createReview(
            Long eventId,
            ReviewDTO dto,
            String userEmail) {

        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        // Guard 1 — event must be COMPLETED
        if (event.getStatus()
                != EventStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Reviews are only allowed for " +
                            "completed events. " +
                            "Event status is: "
                            + event.getStatus());
        }

        // Guard 2 — user must have attended
        boolean attended = registrationRepository
                .existsByUserAndEvent(user, event);

        if (!attended) {
            throw new IllegalArgumentException(
                    "You can only review events " +
                            "you registered for");
        }

        // Guard 3 — one review per event
        if (reviewRepository.existsByUserAndEvent(
                user, event)) {
            throw new IllegalArgumentException(
                    "You have already reviewed this event");
        }

        Review review = Review.builder()
                .user(user)
                .event(event)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        Review saved = reviewRepository.save(review);
        log.info("{} reviewed event {} with rating {}",
                userEmail, eventId, dto.getRating());

        return toDTO(saved);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(
            Long reviewId,
            ReviewDTO dto,
            String userEmail) {

        User user = getUser(userEmail);
        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found: " + reviewId));

        // Guard — only review owner can update
        if (!review.getUser().getId()
                .equals(user.getId())) {
            throw new AccessDeniedException(
                    "You can only edit your own reviews");
        }

        review.setRating(dto.getRating());
        if (dto.getComment() != null) {
            review.setComment(dto.getComment());
        }

        return toDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(
            Long reviewId, String userEmail) {

        User user = getUser(userEmail);
        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found: " + reviewId));

        boolean isOwner = review.getUser().getId()
                .equals(user.getId());
        boolean isAdmin = user.getRole()
                .equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException(
                    "You can only delete your own reviews");
        }

        reviewRepository.delete(review);
        log.info("Review {} deleted by {}",
                reviewId, userEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getEventReviews(
            Long eventId) {
        Event event = getEvent(eventId);
        return reviewRepository
                .findByEventOrderByCreatedAtDesc(event)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getMyReviews(
            String userEmail) {
        User user = getUser(userEmail);
        return reviewRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEventRating(
            Long eventId) {
        Event event = getEvent(eventId);
        Double avg = reviewRepository
                .findAverageRatingByEvent(event);
        long count = reviewRepository
                .countByEvent(event);

        return Map.of(
                "eventId", eventId,
                "averageRating",
                avg != null
                        ? Math.round(avg * 10.0) / 10.0
                        : 0.0,
                "totalReviews", count
        );
    }
}