INSERT INTO users (email, password, name, role, created_at)
VALUES (
    'admin@events.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Admin',
    'ADMIN',
    NOW()
);