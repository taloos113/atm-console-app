USE atm_system;

INSERT INTO accounts (login, pin_code, holder_name, balance, status, role)
VALUES
('admin', '12345', 'System Administrator', 0.00, 'ACTIVE', 'ADMIN'),
('alice', '11111', 'Alice Johnson', 1200.00, 'ACTIVE', 'CUSTOMER'),
('bob', '22222', 'Bob Smith', 850.00, 'ACTIVE', 'CUSTOMER');

INSERT INTO transactions (account_number, transaction_type, amount, balance_after, note)
VALUES
(2, 'DEPOSIT', 200.00, 1200.00, 'Initial sample deposit'),
(3, 'DEPOSIT', 150.00, 850.00, 'Initial sample deposit');