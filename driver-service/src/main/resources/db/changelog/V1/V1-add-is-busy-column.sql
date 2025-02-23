-- liquibase formatted sql

-- changeset Vlad:1737628883187-2
ALTER TABLE driver ADD COLUMN is_busy BOOLEAN NOT NULL DEFAULT FALSE;