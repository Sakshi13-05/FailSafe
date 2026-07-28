package com.failsafe.ingestion.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.failsafe.ingestion.model.Event;

@Component
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class); // Fixed class name!

    @KafkaListener(topics = "raw-ingestion", groupId = "failsafe-group")
    public void consume(Event event) {
        log.info("Received event: {}", event);
    }
}