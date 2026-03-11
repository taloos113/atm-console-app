# ATM Console Application

A Java console-based ATM system with MySQL persistence.

## Features

### Customer
- Login with 5-digit PIN
- Deposit cash
- Withdraw cash
- Display balance
- Transaction receipt display

### Administrator
- Login with 5-digit PIN
- Create new account
- Delete existing account
- Update account information
- Search for account

## Technologies
- Java
- Maven
- MySQL
- JDBC

## Database Setup

1. Run `sql/schema.sql`
2. Run `sql/seed.sql`

## Default Accounts

### Admin
- login: admin
- pin: 12345

### Customer
- login: alice
- pin: 11111

### Customer
- login: bob
- pin: 22222

## Project Structure

- `src/main/java/atm` : source code
- `sql/schema.sql` : database schema
- `sql/seed.sql` : initial sample data

## Notes
- PIN must be exactly 5 digits
- Account data is stored in MySQL
- Transactions are recorded in the `transactions` table