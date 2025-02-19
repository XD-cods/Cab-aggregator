-- liquibase formatted sql

-- changeset Vlad:1738058972462-2
CREATE TABLE IF NOT EXISTS kafka_messages (
  message_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  topic VARCHAR(50) NOT NULL,
  key BIGINT,
  message TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  sent_at TIMESTAMP,
  is_sent BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_outbox_is_sent ON kafka_messages (is_sent);