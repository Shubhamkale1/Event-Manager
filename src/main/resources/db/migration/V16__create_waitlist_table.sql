CREATE TABLE waitlist (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    user_id     BIGINT      NOT NULL,
    event_id    BIGINT      NOT NULL,
    position    INT         NOT NULL,
    status      VARCHAR(50) NOT NULL DEFAULT 'WAITING',
    joined_at   DATETIME    DEFAULT CURRENT_TIMESTAMP,
    notified_at DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY unique_waitlist (user_id, event_id),
    CONSTRAINT fk_waitlist_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_waitlist_event
        FOREIGN KEY (event_id)
        REFERENCES event(id)
        ON DELETE CASCADE
);