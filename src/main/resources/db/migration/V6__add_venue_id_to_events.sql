ALTER TABLE events
ADD COLUMN venue_id BIGINT,
ADD CONSTRAINT fk_event_venue
    FOREIGN KEY (venue_id) REFERENCES venues(id)
    ON DELETE SET NULL;