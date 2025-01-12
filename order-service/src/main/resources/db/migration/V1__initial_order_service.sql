CREATE SCHEMA IF NOT EXISTS orders_service;

CREATE TABLE IF NOT EXISTS orders_service.orders (
    id SERIAL PRIMARY KEY,
    customer VARCHAR(255),
    product VARCHAR(255),
    quantity INTEGER,
    status VARCHAR(50) DEFAULT 'CREATED'
);