package com.failsafe.ingestion.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class Event {
    private UUID eventId;
    private String traceId;
    private String sourceId;
    private String payload;
    private Instant receivedAt;
    private String version;
}