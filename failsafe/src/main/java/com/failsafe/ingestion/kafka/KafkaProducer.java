package com.failsafe.ingestion.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void publish(String topic, String key, Object payload) {
        try {
            // Convert object/map cleanly to a JSON string
            String jsonPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, key, jsonPayload);
            log.info("Published message to topic {} with key {}: {}", topic, key, jsonPayload);
        } catch (Exception e) {
            log.error("Failed to convert and publish message to Kafka", e);
        }
    }
}