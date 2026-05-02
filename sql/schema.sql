CREATE DATABASE IF NOT EXISTS atm_system;
USE atm_system;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    account_number INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(50) NOT NULL UNIQUE,
    pin_code CHAR(5) NOT NULL,
    holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    role VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_pin_format CHECK (CHAR_LENGTH(pin_code) = 5),
    CONSTRAINT chk_role CHECK (role IN ('CUSTOMER', 'ADMIN')),
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'DISABLED')),
    CONSTRAINT chk_balance_nonnegative CHECK (balance >= 0)
);

CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    balance_after DECIMAL(12,2) NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_number)
        REFERENCES accounts(account_number) ON DELETE CASCADE,
    CONSTRAINT chk_transaction_type CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAW')),
    CONSTRAINT chk_transaction_amount CHECK (amount > 0),
    CONSTRAINT chk_balance_after CHECK (balance_after >= 0)
);
