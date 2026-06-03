package com.shubham.event_manager.kafka;

import com.shubham.event_manager.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    public void publishEventCancelled(
            EventCancelledMessage message) {

        CompletableFuture<SendResult<String, Object>>
                future = kafkaTemplate.send(
                KafkaConfig.TOPIC_EVENT_CANCELLED,
                String.valueOf(message.getEventId()),
                message
        );

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(
                        "Published event.cancelled for " +
                                "eventId={} to partition={} offset={}",
                        message.getEventId(),
                        result.getRecordMetadata()
                                .partition(),
                        result.getRecordMetadata()
                                .offset()
                );
            } else {
                log.error(
                        "Failed to publish event.cancelled " +
                                "for eventId={}: {}",
                        message.getEventId(),
                        ex.getMessage()
                );
            }
        });
    }

    public void publishEventRegistered(
            EventRegisteredMessage message) {

        CompletableFuture<SendResult<String, Object>>
                future = kafkaTemplate.send(
                KafkaConfig.TOPIC_EVENT_REGISTERED,
                message.getUserEmail(),
                message
        );

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(
                        "Published event.registered for " +
                                "user={} event={}",
                        message.getUserEmail(),
                        message.getEventId()
                );
            } else {
                log.error(
                        "Failed to publish event.registered: {}",
                        ex.getMessage()
                );
            }
        });
    }
}