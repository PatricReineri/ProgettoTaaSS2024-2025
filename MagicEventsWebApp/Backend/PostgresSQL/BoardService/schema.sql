 BEGIN;

CREATE TABLE board (
    event_id BIGINT PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    username TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    board_event_id BIGINT NOT NULL,
    CONSTRAINT fk_board FOREIGN KEY (board_event_id) REFERENCES board (event_id) ON DELETE CASCADE
);

COMMIT;

