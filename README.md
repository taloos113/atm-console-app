# ATM Console Application

A Java console-based ATM system with MySQL persistence.

## Features

### Customer

- Login with a 5-digit PIN
- Withdraw cash
- Deposit cash
- Display balance
- Print transaction receipt

### Administrator

- Login with a 5-digit PIN
- Create new account
- Delete existing account
- Update account information
- Search for account

## Architecture

This project uses a 4-layer layered architecture:

1. Console UI Layer
2. Service Layer
3. Repository Layer
4. Database Layer

It also uses repository interfaces and constructor-based dependency injection.

## Important Refactoring Updates

- Database connection strings moved to `src/main/resources/config.properties`.
- `IAccountRepository` and `ITransactionRepository` added.
- Services depend on interfaces instead of concrete repository classes.
- Repository layer only handles SQL.
- ResultSet-to-Account mapping moved to `Account.fromResultSet()`.
- `Account` is immutable and avoids setters.
- `App.main()` includes top-level try/catch error handling.
- Unit tests added with JUnit 5 and Mockito.
- JaCoCo coverage check requires at least 90% line coverage for tested layers.
- Checkstyle, EditorConfig, scripts, Javadoc, and GitHub Actions CI are included.

## Database Setup

1. Open MySQL or phpMyAdmin.
2. Run `sql/schema.sql`.
3. Run `sql/seed.sql`.
4. Edit `src/main/resources/config.properties` if your MySQL username or password is different.

Default config:

```properties
db.url=jdbc:mysql://localhost:3306/atm_system?useSSL=false&serverTimezone=UTC
db.username=root
db.password=
```

## Default Accounts

### Admin

- login: `admin`
- pin: `12345`

### Customer

- login: `alice`
- pin: `11111`

### Customer

- login: `bob`
- pin: `22222`

## Run the Application

```bash
mvn clean compile
mvn exec:java
```

## Run Tests and Coverage

```bash
mvn clean verify
```

Coverage report location:

```text
target/site/jacoco/index.html
```

## Generate Documentation

```bash
mvn javadoc:javadoc
```

Javadoc output location:

```text
target/site/apidocs/index.html
```

## Build Script

Windows PowerShell:

```powershell
.\scripts\build.ps1
```

macOS/Linux/Git Bash:

```bash
bash scripts/build.sh
```
