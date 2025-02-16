-- liquibase formatted sql

-- changeset Vlad:1738246713004-1
CREATE TABLE ride_info (
    ride_info_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ride_id BIGINT NOT NULL UNIQUE,
    driver_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    CONSTRAINT pk_ride_info PRIMARY KEY (ride_info_id)
);

CREATE TABLE rating (
    rating_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    rating DOUBLE PRECISION NOT NULL DEFAULT 5,
    ride_info_id BIGINT,
    rated_by INTEGER NOT NULL,
    comment TEXT,
    CONSTRAINT pk_rating PRIMARY KEY (rating_id),
    CONSTRAINT chk_rating_range CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT chk_rating_precision CHECK (rating = ROUND(rating::numeric, 2))
);

ALTER TABLE rating
ADD CONSTRAINT fk_rating_ride_info FOREIGN KEY (ride_info_id) REFERENCES ride_info(ride_info_id) ON DELETE CASCADE;