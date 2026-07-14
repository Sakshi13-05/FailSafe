package com.failsafe.ingestion.mapper;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.model.Event;

@Data
public class EventMapper {

    public Event map(EventRequest req) {
        Event event = new Event();
        event.setEventId(UUID.randomUUID());
        event.setTraceId(UUID.randomUUID().toString());
        event.setSourceId(req.getSourceId());
        event.setPayload(req.getPayload());

        event.setReceivedAt(Instant.now());
        event.setVersion("v1");
        return (event);
    }

}