package com.failsafe.ingestion.kafka;

import com.failsafe.ingestion.entity.EventEntity;
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

    @KafkaListener(topics = "failsafe-events-topic", groupId = "failsafe-group-v3")
    public void consume(ConsumerRecord<String, String> record) {
        String payloadJson = record.value(); // Change parameter to String!
        log.info("Received raw event payload from Kafka: {}", payloadJson, record.offset());
        try {
            String uniqueEventId = java.util.UUID.randomUUID().toString();
            EventEntity entity = new EventEntity();
            entity.setSourceId("kafka-consumer");
            entity.setEventType("SIGNUP_EVENT");
            entity.setPayload(payloadJson);
            entity.setEventId(uniqueEventId); // Populates eventId cleanly without touching the auto-increment primary
                                              // key id
            EventEntity savedEntity = eventRepository.save(entity);

            log.info("Successfully persisted event to MySQL database table [events] with ID: {}", savedEntity.getId());
        } catch (Exception e) {
            log.error("Failed to persist event into MySQL: {}", payloadJson, e);
        }
    }
}