BEGIN;

CREATE TABLE game (
    event_id BIGINT PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE guest_info (
    id SERIAL PRIMARY KEY,
    is_men BOOLEAN NOT NULL,
    age INT NOT NULL,
    is_host_family_member BOOLEAN NOT NULL,
    is_host_associate BOOLEAN NOT NULL,
    have_beard BOOLEAN NOT NULL,
    is_bald BOOLEAN NOT NULL,
    have_glasses BOOLEAN NOT NULL,
    have_dark_hair BOOLEAN NOT NULL,
    user_magic_events_tag VARCHAR(255) NOT NULL,
    game_id BIGINT NOT NULL,
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES game (event_id) ON DELETE CASCADE
);

COMMIT;