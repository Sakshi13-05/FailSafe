package com.failsafe.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.failsafe.failsafe.Event;
import com.failsafe.failsafe.EventMapper;
import com.failsafe.failsafe.EventRequest;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class EventMapperTest {

    @Test
    void shouldConvertEventRequestToEvent() {

        // Arrange
        EventMapper mapper = new EventMapper();

        EventRequest request = new EventRequest();
        request.setSourceId("sensor-1");
        request.setPayload("temperature=32");

        UUID eventId = UUID.randomUUID();
        Instant receivedAt = Instant.now();

        // Act
        Event event = mapper.toEvent(request, eventId, receivedAt);
        
        System.out.println("---------- TEST OUTPUT ----------");
        System.out.println("Original Request: " + request);
        System.out.println("Mapped Event: " + event);
        System.out.println("---------------------------------");

        // Assert
        assertEquals(eventId, event.getEventId());
        assertEquals("sensor-1", event.getSourceId());
        assertEquals("temperature=32", event.getPayload());
        assertEquals(receivedAt, event.getReceivedAt());
    }
}