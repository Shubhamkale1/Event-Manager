package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.EventDTO;

public interface EmailService {

    void sendEventConfirmation(EventDTO eventDTO, String toEmail);

}
