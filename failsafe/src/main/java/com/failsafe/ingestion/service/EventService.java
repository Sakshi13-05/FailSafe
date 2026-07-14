package com.failsafe.ingestion.service;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.model.Event;
import com.failsafe.ingestion.mapper.EventMapper;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    
    private final EventMapper eventMapper;

    public EventService(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Event process(EventRequest req) {
        Event event = eventMapper.map(req);
        return event;
    }
}