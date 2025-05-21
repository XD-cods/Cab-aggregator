-- liquibase formatted sql

-- changeset Vlad:1738058972462-4
 ALTER TABLE ride ALTER COLUMN driver_id DROP NOT NULL;
