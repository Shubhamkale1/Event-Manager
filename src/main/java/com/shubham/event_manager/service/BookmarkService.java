package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.EventDTO;
import java.util.List;

public interface BookmarkService {
    void addBookmark(Long eventId, String userEmail);
    void removeBookmark(Long eventId, String userEmail);
    List<EventDTO> getMyBookmarks(String userEmail);
    boolean isBookmarked(Long eventId, String userEmail);
}