CREATE TABLE IF NOT EXISTS app_user (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(200) NOT NULL UNIQUE,
    password        VARCHAR(200) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    surname         VARCHAR(100) NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    version         BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS event (
    id              BIGSERIAL PRIMARY KEY,
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP NOT NULL,
    title           VARCHAR(100) NOT NULL,
    duration        BIGINT NOT NULL,
    duration_unit   VARCHAR(50) NOT NULL,
    owner_id        BIGINT REFERENCES app_user(id),
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    version         BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS event_participant (
    id              BIGSERIAL PRIMARY KEY,
    participant_id  BIGINT REFERENCES app_user(id),
    event_id        BIGINT REFERENCES event(id),
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    version         BIGINT NOT NULL,
    CONSTRAINT unique_participant UNIQUE(participant_id, event_id)
);