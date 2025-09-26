CREATE TABLE IF NOT EXISTS app_user (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(200) NOT NULL UNIQUE,
    password        VARCHAR(200) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    surname         VARCHAR(100) NOT NULL,
    tz_preference   VARCHAR(50),
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
    CONSTRAINT unique_event_participant UNIQUE(participant_id, event_id)
);

CREATE TABLE IF NOT EXISTS recurring_event (
    id              BIGSERIAL PRIMARY KEY,
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP,
    title           VARCHAR(100) NOT NULL,
    duration        BIGINT NOT NULL,
    duration_unit   VARCHAR(50) NOT NULL,
    repetition      BIGINT NOT NULL,
    repetition_unit VARCHAR(50) NOT NULL,
    owner_id        BIGINT REFERENCES app_user(id),
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    version         BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS recurring_event_participant (
    id                          BIGSERIAL PRIMARY KEY,
    participant_id              BIGINT REFERENCES app_user(id),
    recurring_event_id          BIGINT REFERENCES recurring_event(id),
    created_at                  TIMESTAMP NOT NULL,
    updated_at                  TIMESTAMP NOT NULL,
    version                     BIGINT NOT NULL,
    CONSTRAINT unique_recurring_event_participant UNIQUE(participant_id, recurring_event_id)
);
