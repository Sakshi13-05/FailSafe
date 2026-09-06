package com.failsafe.ingestion.service;

import com.failsafe.ingestion.entity.RetryEventEntity;
import com.failsafe.ingestion.repository.RetryEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FallbackRetryService {

    private static final Logger log = LoggerFactory.getLogger(FallbackRetryService.class);

    @Autowired
    private RetryEventRepository retryRepository;

    public void saveForRetry(String topic, String key, String payload, Throwable ex) {
        try {
            RetryEventEntity retryRecord = new RetryEventEntity();
            retryRecord.setTopic(topic);
            retryRecord.setEventKey(key);
            retryRecord.setPayload(payload);
            retryRecord.setStatus("PENDING");
            retryRecord.setRetryCount(0);

            // First retry in 10 seconds
            retryRecord.setNextRetryAt(LocalDateTime.now().plusSeconds(10));
            retryRecord.setLastError(ex != null ? ex.getMessage() : "Kafka unavailable");

            retryRepository.save(retryRecord);
            log.warn("Safely staged failed event to MySQL retry store for topic: {}", topic);
        } catch (Exception e) {
            log.error("CRITICAL: Failed to write to MySQL fallback store!", e);
        }
    }
}