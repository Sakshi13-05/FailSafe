package com.failsafe.failsafe;

import lombok.Data;

@Data
public class EventRequest {
    private String sourceId;
    private String payload;
}