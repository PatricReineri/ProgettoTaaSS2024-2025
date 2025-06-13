CREATE TABLE user_info (
    magic_events_tag SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_image_url VARCHAR(512),
    name VARCHAR(100),
    surname VARCHAR(100),
    role VARCHAR(100),
    password VARCHAR(500)
);

CREATE TABLE token (
    access_token VARCHAR(255) PRIMARY KEY,
    refresh_token VARCHAR(255),
    magic_events_tag BIGINT,
    CONSTRAINT fk_user
        FOREIGN KEY (magic_events_tag)
        REFERENCES users(magic_events_tag)
        ON DELETE CASCADE
);

