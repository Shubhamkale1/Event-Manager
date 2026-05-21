package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.ReviewDTO;
import java.util.List;
import java.util.Map;

public interface ReviewService {

    ReviewDTO createReview(Long eventId,
                           ReviewDTO dto, String userEmail);

    ReviewDTO updateReview(Long reviewId,
                           ReviewDTO dto, String userEmail);

    void deleteReview(Long reviewId,
                      String userEmail);

    List<ReviewDTO> getEventReviews(Long eventId);

    List<ReviewDTO> getMyReviews(String userEmail);

    Map<String, Object> getEventRating(Long eventId);
}