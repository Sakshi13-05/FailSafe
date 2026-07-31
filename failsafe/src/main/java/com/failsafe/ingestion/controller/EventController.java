package com.failsafe.ingestion.controller;

import com.failsafe.ingestion.dto.ApiResponse;
import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final String TOPIC = "failsafe-events-topic";

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody Map<String, Object> userData) {
        // 1. Send incoming signup data to Kafka topic with key
        String key = String.valueOf(userData.getOrDefault("email", "auth-signup"));
        kafkaProducer.publish(TOPIC, key, userData);

        // 2. Return success response back to React frontend
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Account registration queued successfully via Kafka!"));
    }

    @PostMapping("/event")
    public ResponseEntity<ApiResponse> signupEventRequest(@RequestBody EventRequest userData) {
        // Handle EventRequest DTO payload
        kafkaProducer.publish(TOPIC, userData.getSourceId(), userData);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Account registration queued successfully via Kafka!"));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> submitRating(@RequestBody Map<String, Object> ratingData) {
        // Publish JSON payload to Kafka topic
        String key = String.valueOf(ratingData.getOrDefault("storeId", "rating-event"));
        kafkaProducer.publish(TOPIC, key, ratingData);

        return ResponseEntity.ok(new ApiResponse("Rating successfully submitted to the FailSafe pipeline!"));
    }
}