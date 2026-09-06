package com.failsafe.ingestion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "retry_events")
@Getter
@Setter
public class RetryEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    private String eventKey;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String payload;

    @Column(nullable = false)
    private String status; // PENDING, IN_PROGRESS, SENT, DEAD_LETTER

    @Column(nullable = false)
    private int retryCount = 0;

    @Column(nullable = false)
    private LocalDateTime nextRetryAt;

    @Column(columnDefinition = "TEXT")
    private String lastError;

    private LocalDateTime createdAt = LocalDateTime.now();
}