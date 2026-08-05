package com.failsafe.ingestion.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.failsafe.ingestion.dto.EventRequest;
import com.failsafe.ingestion.entity.EventEntity;
import com.failsafe.ingestion.mapper.EventMapper;
import com.failsafe.ingestion.repository.EventRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

    // Matches your requested topic and group
    @KafkaListener(topics = "failsafe-events-topic", groupId = "failsafe-group-v4")
    public void consume(ConsumerRecord<String, String> record) {
        String payloadJson = record.value();
        log.info("Received event from Kafka topic [failsafe-events-topic] at offset: {}", record.offset());

        try {
            // 1. Deserialize: Convert raw JSON string to EventRequest DTO
            EventRequest request = objectMapper.readValue(payloadJson, EventRequest.class);

            // 2. Map: Convert DTO to Entity using our Mapper
            EventEntity entity = eventMapper.toEntity(request);

            // 3. Persist: Save to MySQL
            EventEntity savedEntity = eventRepository.save(entity);

            log.info("Successfully persisted to MySQL. DB Primary Key: {}, Event Logical ID: {}",
                    savedEntity.getId(), savedEntity.getEventId());
        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}. Error: {}", payloadJson, e.getMessage());
        }
    }
}