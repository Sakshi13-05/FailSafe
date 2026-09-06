package com.failsafe.ingestion.service;

import com.failsafe.ingestion.entity.RetryEventEntity;
import com.failsafe.ingestion.repository.RetryEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RetryWorkerService {

    private static final Logger log = LoggerFactory.getLogger(RetryWorkerService.class);

    private static final int MAX_RETRIES = 5;
    private static final int BASE_BACKOFF_SECONDS = 5;

    @Autowired
    private RetryEventRepository retryRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Runs every 5 seconds in the background
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processPendingRetries() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Fetch records that are PENDING and past their backoff timestamp
        List<RetryEventEntity> pendingEvents = retryRepository.findByStatusAndNextRetryAtBefore("PENDING", now);

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Found {} failed events ready for retry.", pendingEvents.size());

        for (RetryEventEntity event : pendingEvents) {
            try {
                // 2. Attempt to publish again to Kafka (synchronous send here to confirm
                // delivery)
                kafkaTemplate.send(event.getTopic(), event.getEventKey(), event.getPayload()).get();

                // 3. Success: Mark as SENT
                event.setStatus("SENT");
                log.info("Successfully recovered and resent event ID: {} to Kafka", event.getId());

            } catch (Exception ex) {
                // 4. Failure: Apply Exponential Backoff or send to Dead Letter
                handleRetryFailure(event, ex);
            }

            retryRepository.save(event);
        }
    }

    private void handleRetryFailure(RetryEventEntity event, Exception ex) {
        int currentAttempts = event.getRetryCount() + 1;
        event.setRetryCount(currentAttempts);
        event.setLastError(ex.getMessage());

        if (currentAttempts >= MAX_RETRIES) {
            // Reached max retries: Route to DEAD_LETTER for manual inspection
            event.setStatus("DEAD_LETTER");
            log.error("Event ID: {} exceeded MAX_RETRIES ({}). Marked as DEAD_LETTER.",
                    event.getId(), MAX_RETRIES);
        } else {
            // Exponential Backoff formula: BASE_SECONDS * (2 ^ currentAttempts)
            // Attempt 1: 5 * 2^1 = 10s
            // Attempt 2: 5 * 2^2 = 20s
            // Attempt 3: 5 * 2^3 = 40s
            // Attempt 4: 5 * 2^4 = 80s
            long backoffSeconds = (long) (BASE_BACKOFF_SECONDS * Math.pow(2, currentAttempts));
            event.setNextRetryAt(LocalDateTime.now().plusSeconds(backoffSeconds));

            log.warn("Retry failed for event ID: {}. Scheduling attempt {} in {} seconds.",
                    event.getId(), currentAttempts + 1, backoffSeconds);
        }
    }
}