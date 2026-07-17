package com.failsafe.ingestion.kafka;

import com.failsafe.ingestion.model.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Event event) {
        kafkaTemplate.send("raw-ingestion", event);
    }
}