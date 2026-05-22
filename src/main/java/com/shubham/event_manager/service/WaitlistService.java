package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.WaitlistDTO;
import java.util.List;

public interface WaitlistService {
    WaitlistDTO joinWaitlist(Long eventId,
                             String userEmail);
    void leaveWaitlist(Long eventId,
                       String userEmail);
    List<WaitlistDTO> getEventWaitlist(Long eventId);
    List<WaitlistDTO> getMyWaitlist(String userEmail);
    void promoteFromWaitlist(Long eventId);
}