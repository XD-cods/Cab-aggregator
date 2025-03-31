-- liquibase formatted sql

-- changeset Vlad:1738058972462-3
ALTER TABLE kafka_messages ADD COLUMN traceparent TEXT;
