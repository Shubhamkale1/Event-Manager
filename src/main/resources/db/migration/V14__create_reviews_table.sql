CREATE TABLE reviews (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     BIGINT          NOT NULL,
    event_id    BIGINT          NOT NULL,
    rating      INT             NOT NULL,
    comment     VARCHAR(1000),
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY unique_review (user_id, event_id),
    CONSTRAINT fk_review_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_review_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,
    CONSTRAINT chk_rating
        CHECK (rating BETWEEN 1 AND 5)
);