package com.failsafe.ingestion.service;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.entity.EventEntity;
import com.failsafe.ingestion.mapper.EventMapper;
import com.failsafe.ingestion.kafka.KafkaProducer;
import com.failsafe.ingestion.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventMapper eventMapper;
    private final KafkaProducer kafkaProducer;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public EventService(EventMapper eventMapper, KafkaProducer kafkaProducer,
            EventRepository eventRepository, ObjectMapper objectMapper) {
        this.eventMapper = eventMapper;
        this.kafkaProducer = kafkaProducer;
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * PRODUCER FLOW: Called by Controller
     */
    public EventEntity process(EventRequest req) {
        // 1. Map Request to Entity (Type must be EventEntity)
        EventEntity entity = eventMapper.toEntity(req);

        String key = req.getSourceId();
        String topic = "failsafe-events-topic";

        // 2. Publish the object to Kafka
        kafkaProducer.publish(topic, key, entity);

        return entity;
    }

    /**
     * CONSUMER FLOW: Triggered by Kafka
     */
    @KafkaListener(topics = "failsafe-events-topic", groupId = "failsafe-group-v4")
    public void consumeEvent(String message) {
        log.info("Received message from Kafka: {}", message);

        try {
            // 1. Convert JSON back to Entity
            // NOTE: If you send an 'EventEntity' via Producer, Jackson needs to read it as
            // 'EventEntity'
            EventEntity eventEntity = objectMapper.readValue(message, EventEntity.class);

            // 2. Save to MySQL
            eventRepository.save(eventEntity);

            log.info("Successfully saved event to Database with ID: {}", eventEntity.getEventId());
        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", e.getMessage());
        }
    }
}