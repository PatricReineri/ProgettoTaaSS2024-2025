-- Tabella utenti
CREATE TABLE user_info (
    magic_events_tag SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Tabella eventi
CREATE TABLE event_info (
    event_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    starting VARCHAR(50) NOT NULL,
    ending VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    magic_events_tag BIGINT,
    FOREIGN KEY (magic_events_tag) REFERENCES user_info(magic_events_tag)
);

-- Tabella admin
CREATE TABLE admins (
    admin_id VARCHAR(64) PRIMARY KEY,
    magic_events_tag BIGINT,
    FOREIGN KEY (magic_events_tag) REFERENCES user_info(magic_events_tag)
);

-- Tabella di join ManyToMany tra eventi e admin
CREATE TABLE event_admins (
    event_id BIGINT,
    admin_id VARCHAR(64),
    PRIMARY KEY (event_id, admin_id),
    FOREIGN KEY (event_id) REFERENCES event_info(event_id),
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
);