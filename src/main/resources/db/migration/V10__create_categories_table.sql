CREATE TABLE categories (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL UNIQUE,
    description VARCHAR(500),
    icon        VARCHAR(100),
    created_at  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE event_categories (
    event_id    BIGINT  NOT NULL,
    category_id BIGINT  NOT NULL,
    PRIMARY KEY (event_id, category_id),
    CONSTRAINT fk_ec_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ec_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE
);

INSERT INTO categories (name, description, icon)
VALUES
    ('Technology', 'Software, hardware, and tech events', 'laptop'),
    ('Education', 'Workshops, seminars, and learning events', 'book'),
    ('Business', 'Networking, entrepreneurship, and finance', 'briefcase'),
    ('Music', 'Concerts, open mics, and music workshops', 'music'),
    ('Sports', 'Tournaments, fitness, and outdoor activities', 'activity'),
    ('Arts', 'Exhibitions, performances, and creative workshops', 'palette'),
    ('Food', 'Food festivals, cooking classes, and tastings', 'coffee'),
    ('Health', 'Wellness, mental health, and fitness events', 'heart'),
    ('Social', 'Community gatherings and social meetups', 'users'),
    ('Other', 'Everything else', 'grid');