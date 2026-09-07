package com.failsafe.ingestion.kafka;

import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.entity.EventEntity;
import com.failsafe.ingestion.mapper.EventMapper;
import com.failsafe.ingestion.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ObjectMapper objectMapper;

    // Retries 3 times with exponential backoff, then routes to DLT
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 2000, multiplier = 2.0),
        dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "failsafe-events-topic", groupId = "failsafe-group-v4")
    public void consume(ConsumerRecord<String, String> record) throws Exception {
        String payloadJson = record.value();
        log.info("Processing event from offset: {}", record.offset());

        // 1. Deserialization
        EventRequest request = objectMapper.readValue(payloadJson, EventRequest.class);

        // 2. Mapping
        EventEntity entity = eventMapper.toEntity(request);

        // 3. Database Persistence (Exceptions thrown here trigger the retry/DLT flow)
        EventEntity savedEntity = eventRepository.save(entity);
        log.info("Successfully persisted entity ID: {}", savedEntity.getId());
    }

    // Handles records that failed all 3 retries (Poison Pills or persistent DB outages)
    @DltHandler
    public void handleDeadLetterRecord(ConsumerRecord<String, String> record) {
        log.error("CRITICAL: Message at offset {} routed to DEAD LETTER TOPIC (DLT). Payload: {}", 
                  record.offset(), record.value());
    }
}