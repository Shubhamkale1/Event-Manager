package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.RegistrationDTO;
import java.util.List;

public interface RegistrationService {

    RegistrationDTO registerForEvent(
            Long eventId, String userEmail);

    void cancelRegistration(
            Long eventId, String userEmail);

    List<RegistrationDTO> getMyRegistrations(
            String userEmail);

    List<RegistrationDTO> getEventRegistrations(
            Long eventId, String requestingUserEmail);

    boolean isUserRegistered(
            Long eventId, String userEmail);

    long getConfirmedCount(Long eventId);
}