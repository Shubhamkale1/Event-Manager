package com.shubham.event_manager.service.impl;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendEventConfirmation(EventDTO eventDTO, String toEmail){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Event Created: " + eventDTO.getTitle());
            message.setText(
                    "Your event has been created successfully!\n\n" +
                            "Event: " + eventDTO.getTitle() + "\n" +
                            "Date: " + eventDTO.getEventDate() + "\n" +
                            "Location: " + eventDTO.getLocation() + "\n" +
                            "Capacity: " + eventDTO.getCapacity()
            );

            mailSender.send(message);
            log.info("Confirmation email send to {}", toEmail);
        }catch (Exception e){
            log.error("Failed to send email to {}: {}",toEmail,e.getMessage());
        }
    }
}
