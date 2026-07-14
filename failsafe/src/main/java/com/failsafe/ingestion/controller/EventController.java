package com.failsafe.ingestion.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.model.Event;
import com.failsafe.ingestion.service.EventService;

@RestController
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public Event createEvent(@RequestBody EventRequest request) {
        Event event = service.process(request);
        return (event);
    }

}
