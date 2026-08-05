CREATE TABLE IF NOT EXISTS events (
    id BIGINT NOT NULL AUTO_INCREMENT,
    event_id VARCHAR(255),
    source_id VARCHAR(255),
    event_type VARCHAR(255),
    payload TEXT,
    created_at DATETIME(6),
    PRIMARY KEY (id)
);
