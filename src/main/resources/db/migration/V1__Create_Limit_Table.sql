CREATE TABLE limits (
    id BIGSERIAL PRIMARY KEY,
    limit_sum DECIMAL(19, 2) NOT NULL,
    limit_datetime TIMESTAMP NOT NULL,
    currency_shortname VARCHAR(3) NOT NULL,
    expense_category VARCHAR(255) NOT NULL,
    account BIGINT NOT NULL
);
