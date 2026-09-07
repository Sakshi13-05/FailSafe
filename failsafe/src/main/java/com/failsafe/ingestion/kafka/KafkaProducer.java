package com.failsafe.ingestion.kafka;

import com.failsafe.ingestion.service.FallbackRetryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FallbackRetryService fallbackRetryService;

    public void publish(String topic, String key, Object payload) {
        try {
            // 1. Convert payload object to JSON string
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // 2. Asynchronous send returns a CompletableFuture
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, jsonPayload);

            // 3. Attach non-blocking completion callback
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    // FAILURE PATH: Kafka broker is down / timeout / buffer full
                    log.error("Kafka send failed for topic [{}] and key [{}]. Diverting to durable fallback store.",
                            topic, key, ex);

                    // Persist to MySQL retry table for background worker recovery
                    fallbackRetryService.saveForRetry(topic, key, jsonPayload, ex);

                } else {
                    // SUCCESS PATH: Broker acknowledged message receipt
                    log.info("Message successfully delivered to topic [{}] | partition [{}] | offset [{}]",
                            topic,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });

        } catch (Exception e) {
            // Catches local JSON serialization errors before hitting network buffer
            log.error("Immediate failure occurred before sending message to Kafka topic [{}]: {}", topic, e.getMessage());
        }
    }
}