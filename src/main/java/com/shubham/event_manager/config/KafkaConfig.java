package com.shubham.event_manager.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    // Topic names as constants
    // Use these everywhere instead of string literals
    public static final String TOPIC_EVENT_CANCELLED
            = "event.cancelled";
    public static final String TOPIC_EVENT_REGISTERED
            = "event.registered";
    public static final String TOPIC_EMAIL_REQUESTED
            = "email.requested";

    @Bean
    public NewTopic eventCancelledTopic() {
        return TopicBuilder
                .name(TOPIC_EVENT_CANCELLED)
                .partitions(3)
                .replicas(1)
                .build();
    }
    // partitions(3) — 3 partitions for parallel processing
    // replicas(1) — 1 replica (we only have 1 Kafka broker)
    // Production: replicas(3) with 3 brokers

    @Bean
    public NewTopic eventRegisteredTopic() {
        return TopicBuilder
                .name(TOPIC_EVENT_REGISTERED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic emailRequestedTopic() {
        return TopicBuilder
                .name(TOPIC_EMAIL_REQUESTED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}