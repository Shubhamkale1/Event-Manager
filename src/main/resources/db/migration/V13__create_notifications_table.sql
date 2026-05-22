CREATE TABLE notifications (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     BIGINT          NOT NULL,
    type        VARCHAR(100)    NOT NULL,
    title       VARCHAR(255)    NOT NULL,
    message     VARCHAR(1000)   NOT NULL,
    is_read     BOOLEAN         NOT NULL DEFAULT FALSE,
    entity_type VARCHAR(100),
    entity_id   BIGINT,
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    read_at     DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    INDEX idx_user_unread (user_id, is_read),
    INDEX idx_user_created (user_id, created_at)
);