# ATM Console Application Architecture

## Software Architecture

This project uses a **4-layer layered architecture** with the Repository pattern and constructor-based dependency injection.

1. **Console UI Layer**
   - `MainMenu`
   - `CustomerMenu`
   - `AdminMenu`
   - Responsibility: user input/output only.

2. **Service Layer**
   - `AuthService`
   - `CustomerService`
   - `AdminService`
   - Responsibility: validation and business rules.

3. **Repository Layer**
   - `IAccountRepository`, `AccountRepository`
   - `ITransactionRepository`, `TransactionRepository`
   - Responsibility: SQL and persistence only.

4. **Database Layer**
   - `DBConnection`
   - MySQL database: `atm_system`
   - Responsibility: database connection and stored data.

## Design Decisions

- The repository layer is intentionally thin and only contains SQL-related logic.
- `Account.fromResultSet(resultSet)` is placed in the model layer, not the repository layer.
- Services depend on repository interfaces, not concrete repository classes.
- `Account` is immutable and has no setters. Updates create a new account object using methods such as `withBalance()` or `withProfile()`.
- Database connection strings are stored in `src/main/resources/config.properties` instead of being hard-coded.
- `App.main()` wraps the entry point in a try/catch block for robust error handling.


##  Software Architecture

This project uses a **4-layer layered architecture** with the **Repository Pattern** and **constructor-based dependency injection**.

The four layers are:

1. **Console UI Layer**
   - Includes `MainMenu`, `CustomerMenu`, and `AdminMenu`.
   - Handles console input, menu display, and user output only.

2. **Service Layer**
   - Includes `AuthService`, `CustomerService`, and `AdminService`.
   - Handles validation, business rules, and application logic.

3. **Repository Layer**
   - Includes `IAccountRepository`, `AccountRepository`, `ITransactionRepository`, and `TransactionRepository`.
   - Handles SQL and persistence logic only.

4. **Database Layer**
   - Includes `DBConnection`, `config.properties`, and the MySQL database.
   - Handles database connection and stored data.

The Repository Pattern separates database access from business logic. The service layer depends on repository interfaces rather than concrete repository classes, which supports dependency injection and reduces coupling.

---

##  Code Refactoring Based on Design

The code was refactored based on the system design, object design, and instructor feedback.

The main refactoring changes include:

- Database connection strings were moved into `src/main/resources/config.properties`.
- Repository interfaces were added: `IAccountRepository` and `ITransactionRepository`.
- Service classes were updated to depend on repository interfaces instead of concrete repository classes.
- Constructor-based dependency injection was used in the service layer.
- `ResultSet` mapping was moved into the `Account` model through `Account.fromResultSet()`.
- The `Account` model was refactored to use private final fields and avoid setters.
- Controlled update methods such as `withBalance()` and `withProfile()` were added.
- Repository classes were kept focused on SQL and persistence logic only.
- The main entry point in `App.main()` was wrapped in a try/catch block for unexpected runtime errors.

These changes improve encapsulation, reduce coupling, and make the code structure better match the layered architecture.

---

##  Unit Tests and Code Coverage

The project includes unit tests for public behavior in the model, service, and utility layers.

Test location:

`src/test/java`

Testing tools used:

- JUnit 5
- Mockito
- JaCoCo

The project can be verified using:

`mvn clean verify`

The final test result shows:

`Tests run: 66, Failures: 0, Errors: 0, Skipped: 0`

The JaCoCo coverage report is generated at:

`target/site/jacoco/index.html`

The final line coverage is above the required 90% threshold. This satisfies the code coverage requirement.

---

## Development Tools, Documentation, Build, and CI/CD

The project includes the required development tools and equivalent Java-based tooling.

### EditorConfig

The project includes:

`.editorconfig`

This file keeps formatting consistent across editors by controlling indentation, line endings, encoding, and whitespace rules.

### Linter and Formatting Check

The project uses Checkstyle as the Java linter.

Relevant files:

- `checkstyle.xml`
- `pom.xml`

Checkstyle runs during Maven verification and checks formatting and basic coding style. The final build passes with no Checkstyle violations.

### Documentation

The project uses Javadocs as the Java equivalent of public class and method documentation.

Javadocs can be generated with:

`mvn javadoc:javadoc`

Generated documentation location:

`target/site/apidocs/index.html`

### Build System

The project uses Maven as the main build system.

Main build file:

`pom.xml`

Main build command:

`mvn clean verify`

The project also includes build scripts:

- `scripts/build.ps1`
- `scripts/build.sh`

These scripts provide convenient PowerShell and Bash build commands.

### CI/CD

The project includes a GitHub Actions workflow:

`.github/workflows/ci.yml`

The CI/CD workflow is designed to run automatically when code is pushed to GitHub. It runs the build, style checks, unit tests, coverage checks, and documentation generation.

### Coding Style

The project follows good coding style by applying separation of concerns, repository interfaces, dependency injection, and data hiding.

Examples include:

- Service classes depend on repository interfaces.
- Repository classes focus only on SQL and persistence.
- The `Account` model uses private final fields.
- The `Account` model avoids setters and uses controlled update methods instead.
- The code structure separates UI, service, repository, and database responsibilities.
