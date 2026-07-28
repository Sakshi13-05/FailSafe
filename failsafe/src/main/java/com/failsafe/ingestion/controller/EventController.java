package com.failsafe.ingestion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.model.Event;
import com.failsafe.ingestion.service.EventService;

import jakarta.validation.Valid;

@RestController
@RestControllerAdvice
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/events")

    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventRequest request) {
        Event event = service.process(request);
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .body("Invalid input data");
    }

}
