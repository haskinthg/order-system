CREATE SCHEMA IF NOT EXISTS payments_service;

CREATE TABLE IF NOT EXISTS payments_service.payments (
    id SERIAL PRIMARY KEY,
    order_id BIGINT,
    amount INTEGER,
    status VARCHAR(50) DEFAULT 'NEW'
);