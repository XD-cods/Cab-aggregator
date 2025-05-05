-- liquibase formatted sql

-- changeset Vlad:1738058972462-3
ALTER TABLE kafka_messages ADD COLUMN trace_id TEXT;
ALTER TABLE kafka_messages ADD COLUMN span_id TEXT;
