package com.failsafe.ingestion.repository;

import com.failsafe.ingestion.entity.RetryEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RetryEventRepository extends JpaRepository<RetryEventEntity, Long> {

    // Finds all failed records ready to be retried by the background worker
    List<RetryEventEntity> findByStatusAndNextRetryAtBefore(String status, LocalDateTime currentTime);
}