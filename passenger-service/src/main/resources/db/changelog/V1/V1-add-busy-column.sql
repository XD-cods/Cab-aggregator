-- liquibase formatted sql

-- changeset Влад:1737108790202-2
ALTER TABLE passenger ADD COLUMN is_busy BOOLEAN NOT NULL DEFAULT false;