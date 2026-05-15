ALTER TABLE events
ADD COLUMN registrations_count INT NOT NULL DEFAULT 0;

CREATE TABLE registrations (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    user_id         BIGINT      NOT NULL,
    event_id        BIGINT      NOT NULL,
    status          VARCHAR(50) NOT NULL DEFAULT 'CONFIRMED',
    registered_at   DATETIME    DEFAULT CURRENT_TIMESTAMP,
    cancelled_at    DATETIME,
    notes           VARCHAR(500),
    PRIMARY KEY (id),
    UNIQUE KEY unique_registration (user_id, event_id),
    CONSTRAINT fk_reg_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_reg_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE
);