-- liquibase formatted sql

-- changeset Влад:1737108790202-3
ALTER TABLE passenger ADD COLUMN keycloak_id TEXT NOT NULL;
