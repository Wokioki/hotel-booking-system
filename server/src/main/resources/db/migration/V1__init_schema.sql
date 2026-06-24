CREATE TABLE app_users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE room_types
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rooms
(
    id              BIGSERIAL PRIMARY KEY,
    room_number     VARCHAR(50)    NOT NULL UNIQUE,
    room_type_id    BIGINT         NOT NULL,
    capacity        INTEGER        NOT NULL,
    price_per_night NUMERIC(10, 2) NOT NULL,
    status          VARCHAR(50)    NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rooms_room_type
        FOREIGN KEY (room_type_id)
            REFERENCES room_types (id),

    CONSTRAINT chk_rooms_capacity_positive
        CHECK (capacity > 0),

    CONSTRAINT chk_rooms_price_positive
        CHECK (price_per_night > 0)
);

CREATE TABLE bookings
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT         NOT NULL,
    room_id     BIGINT         NOT NULL,
    check_in    DATE           NOT NULL,
    check_out   DATE           NOT NULL,
    status      VARCHAR(50)    NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookings_user
        FOREIGN KEY (user_id)
            REFERENCES app_users (id),

    CONSTRAINT fk_bookings_room
        FOREIGN KEY (room_id)
            REFERENCES rooms (id),

    CONSTRAINT chk_booking_dates
        CHECK (check_out > check_in),

    CONSTRAINT chk_booking_total_price_positive
        CHECK (total_price > 0)
);

CREATE INDEX idx_rooms_room_type_id ON rooms (room_type_id);
CREATE INDEX idx_bookings_user_id ON bookings (user_id);
CREATE INDEX idx_bookings_room_id ON bookings (room_id);
CREATE INDEX idx_bookings_room_dates ON bookings (room_id, check_in, check_out);
CREATE INDEX idx_bookings_status ON bookings (status);