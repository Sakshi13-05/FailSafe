package com.failsafe.failsafe;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class Event {
    private UUID eventId;
    private String sourceId; // Fixed naming to camelCase
    private String payload;
    private Instant receivedAt;
}