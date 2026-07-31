package com.failsafe.ingestion.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.failsafe.ingestion.entity.EventEntity;
import com.failsafe.ingestion.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "failsafe-events-topic", groupId = "failsafe-group")
    public void consume(Object event) {
        log.info("Received event from Kafka topic [failsafe-events-topic]: {}", event);
        try {
            String payloadJson = objectMapper.writeValueAsString(event);

            // Persist event into MySQL via Hibernate / Spring Data JPA
            EventEntity entity = new EventEntity("kafka-consumer", "GENERIC_EVENT", payloadJson);
            EventEntity savedEntity = eventRepository.save(entity);

            log.info("Successfully persisted event to MySQL database table [events] with ID: {}", savedEntity.getId());
        } catch (Exception e) {
            log.error("Failed to process and persist event from Kafka into MySQL: {}", event, e);
        }
    }
}