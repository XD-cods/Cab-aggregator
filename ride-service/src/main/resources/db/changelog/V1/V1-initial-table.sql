-- liquibase formatted sql

-- changeset Vlad:1738058972462-1
CREATE TABLE IF NOT EXISTS ride (
    ride_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_address VARCHAR(255) NOT NULL,
    finish_address VARCHAR(255) NOT NULL,
    driver_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    ride_status INTEGER NOT NULL,
    ride_price DECIMAL NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE,
    finish_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_ride PRIMARY KEY (ride_id),
    CONSTRAINT fk_ride_driver FOREIGN KEY (driver_id) REFERENCES driver (driver_id),
    CONSTRAINT fk_ride_passenger FOREIGN KEY (passenger_id) REFERENCES passenger (passenger_id)
);

