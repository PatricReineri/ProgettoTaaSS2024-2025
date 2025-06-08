CREATE TABLE users (
    magic_event_tag SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_image_url VARCHAR(512),
    name VARCHAR(100),
    surname VARCHAR(100)
);

