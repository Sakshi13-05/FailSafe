package com.failsafe.failsafe;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

@Component
public class EventMapper {
    public Event toEvent(EventRequest request, UUID eventId, Instant receivedAt) {
        return Event.builder()
                .eventId(eventId)
                .payload(request.getPayload())
                .sourceId(request.getSourceId()) // Fixed case sensitivity
                .receivedAt(receivedAt)
                .build(); // Added missing semicolon
    }
}