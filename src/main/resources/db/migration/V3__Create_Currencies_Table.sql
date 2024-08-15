CREATE TABLE currencies (
    id BIGSERIAL PRIMARY KEY,
    currency_code VARCHAR(3) NOT NULL,
    rate DECIMAL(19, 6) NOT NULL,
    time_stamp TIMESTAMP NOT NULL
);