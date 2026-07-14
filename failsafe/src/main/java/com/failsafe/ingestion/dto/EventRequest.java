package com.failsafe.ingestion.dto;

import lombok.Data;

@Data
public class EventRequest {
    private String sourceId;
    private String payload;
}