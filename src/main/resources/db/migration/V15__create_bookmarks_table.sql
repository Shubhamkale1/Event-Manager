CREATE TABLE bookmarks (
    user_id     BIGINT      NOT NULL,
    event_id    BIGINT      NOT NULL,
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, event_id),
    CONSTRAINT fk_bookmark_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_bookmark_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE
);