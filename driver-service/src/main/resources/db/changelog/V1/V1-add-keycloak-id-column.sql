-- liquibase formatted sql

-- changeset Влад:1737628883187-3
ALTER TABLE driver ADD COLUMN keycloak_id TEXT NOT NULL;
