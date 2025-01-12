CREATE TABLE IF NOT EXISTS deliveries (
    id SERIAL PRIMARY KEY,
    order_id BIGINT,
    payment_id BIGINT,
    status VARCHAR(50) DEFAULT 'NEW'
);