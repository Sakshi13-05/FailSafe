package com.failsafe.ingestion.service;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.model.Event;
import com.failsafe.ingestion.mapper.EventMapper;
import com.failsafe.ingestion.kafka.KafkaProducer;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    
    private final EventMapper eventMapper;
    private final KafkaProducer kafkaProducer;

    public EventService(EventMapper eventMapper,KafkaProducer kafkaProducer) {
        this.eventMapper = eventMapper;
        this.kafkaProducer=kafkaProducer;
    }


    public Event process(EventRequest req) {
        Event event = eventMapper.map(req);
        kafkaProducer.publish(event);
        return event;
    }
}