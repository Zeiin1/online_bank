CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_from BIGINT NOT NULL,
    account_to BIGINT NOT NULL,
    currency_shortname VARCHAR(3) NOT NULL,
    sum DECIMAL(19, 2) NOT NULL,
    expense_category VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    limit_exceeded BOOLEAN,
    limit_id BIGINT,
    CONSTRAINT fk_transaction_limit FOREIGN KEY (limit_id) REFERENCES "limits"(id) ON DELETE CASCADE
);
