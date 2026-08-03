package com.failsafe.ingestion.service;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.entity.EventEntity;
import com.failsafe.ingestion.model.Event;
import com.failsafe.ingestion.mapper.EventMapper;
import com.failsafe.ingestion.kafka.KafkaProducer;
import com.failsafe.ingestion.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventMapper eventMapper;
    private final KafkaProducer kafkaProducer;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public EventService(EventMapper eventMapper, KafkaProducer kafkaProducer, EventRepository eventRepository,
            ObjectMapper objectMapper) {
        this.eventMapper = eventMapper;
        this.kafkaProducer = kafkaProducer;
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "failsafe-events-topic", groupId = "failsafe-group")
    public void consumeEvent(String message) {
        System.out.println("----------------------------------------");
        System.out.println("Received message from Kafka: " + message);
        System.out.println("----------------------------------------");

        try {
            // Convert incoming Kafka JSON string payload directly into your EventEntity
            EventEntity eventEntity = objectMapper.readValue(message, EventEntity.class);

            // Save the record securely to your AWS RDS MySQL database!
            eventRepository.save(eventEntity);

            System.out.println("Successfully saved event entity to AWS RDS database!");
        } catch (Exception e) {
            System.err.println("Failed to process and save Kafka message: " + e.getMessage());
        }
    }

    public Event process(EventRequest req) {
        Event event = eventMapper.map(req);
        String key = req.getSourceId();
        String topic = "failsafe-events-topic";
        kafkaProducer.publish(topic, key, event);
        return event;
    }
}