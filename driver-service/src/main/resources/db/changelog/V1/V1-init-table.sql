-- liquibase formatted sql

-- changeset Vlad:1737628883187-1
CREATE TABLE IF NOT EXISTS driver (
    driver_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    gender INTEGER NOT NULL DEFAULT 4,
    phone VARCHAR(20) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_driver PRIMARY KEY (driver_id)
);

-- changeset Vlad:1737628883187-2
CREATE TABLE IF NOT EXISTS car (
    car_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    color VARCHAR(50) NOT NULL,
    car_brand VARCHAR(50) NOT NULL,
    car_number VARCHAR(50) NOT NULL,
    driver_id BIGINT,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_car PRIMARY KEY (car_id)
);

-- changeset Vlad:1737628883187-3
ALTER TABLE car ADD CONSTRAINT fk_car_driver FOREIGN KEY (driver_id) REFERENCES driver (driver_id);
