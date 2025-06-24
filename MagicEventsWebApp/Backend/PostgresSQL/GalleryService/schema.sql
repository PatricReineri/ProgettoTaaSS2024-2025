BEGIN;

CREATE TABLE gallery (
    event_id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE image (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    uploaded_by VARCHAR(255) NOT NULL,
    base64_image VARCHAR(10485760) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    gallery_event_id BIGINT NOT NULL,
    CONSTRAINT fk_gallery
        FOREIGN KEY (gallery_event_id)
            REFERENCES gallery(event_id)
            ON DELETE CASCADE
);

CREATE TABLE image_user_like (
    id SERIAL PRIMARY KEY,
    user_magic_events_tag VARCHAR(255) NOT NULL,
    image_id BIGINT NOT NULL,
    CONSTRAINT fk_image
        FOREIGN KEY (image_id)
            REFERENCES image(id)
            ON DELETE CASCADE,
	CONSTRAINT uq_image_user_like
        UNIQUE (user_magic_events_tag, image_id)
);

COMMIT;