package com.failsafe.ingestion.mapper;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.entity.EventEntity;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class EventMapper {

    public EventEntity toEntity(EventRequest req) {
        if (req == null) {
            return null;
        }

        EventEntity entity = new EventEntity();

        // Logical ID: Unique identifier for this specific event
        entity.setEventId(UUID.randomUUID().toString());

        // Mapping from Request DTO
        entity.setSourceId(req.getSourceId());
        entity.setPayload(req.getPayload());

        // Defaulting the event type for this flow
        entity.setEventType("RAW_INGEST_STREAM");

        // Note: createdAt is handled by @PrePersist in EventEntity

        return entity;
    }
}