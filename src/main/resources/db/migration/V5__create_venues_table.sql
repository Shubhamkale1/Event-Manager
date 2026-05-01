CREATE TABLE venues (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255)    NOT NULL,
    address     VARCHAR(500)    NOT NULL,
    city        VARCHAR(100)    NOT NULL,
    state       VARCHAR(100),
    pincode     VARCHAR(20),
    capacity    INT,
    latitude    DECIMAL(10, 8),
    longitude   DECIMAL(11, 8),
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE event
ADD COLUMN venue_id BIGINT,
ADD CONSTRAINT fk_event_venue
    FOREIGN KEY (venue_id) REFERENCES venues(id)
    ON DELETE SET NULL;