package com.failsafe.ingestion.kafka;

import com.failsafe.ingestion.model.Event;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Event event) {
        CompletableFuture<SendResult<String, Event>> future = kafkaTemplate.send("raw-ingestion", event);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                // Success path
                log.info("Successfully sent event to topic: {} partition: {} offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                // Failure path
                log.error("Failed to send event: {}", event, exception);
            }
        });
    }
}