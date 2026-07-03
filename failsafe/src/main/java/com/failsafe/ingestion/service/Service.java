package com.failsafe.ingestion.service;

import com.failsafe.failsafe.Event;
import com.failsafe.failsafe.EventMapper;
import com.failsafe.failsafe.EventRequest;

import java.time.Instant;
import java.util.UUID;

public class Service {
    private final EventMapper eventMapper;

    public Service(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Event process(EventRequest request) {
        UUID eventId = UUID.randomUUID();
        Instant receivedAt = Instant.now();
        Event event = eventMapper.toEvent(request, eventId, receivedAt);
        return event;
    }
}
