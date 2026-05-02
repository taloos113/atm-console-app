package atm.service;

import atm.model.Account;
import atm.repository.IAccountRepository;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Handles administrator account management business rules.
 */
public class AdminService {
    private final IAccountRepository accountRepository;

    /**
     * Creates an admin service using an account repository interface.
     *
     * @param accountRepository account repository dependency
     */
    public AdminService(IAccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
    }

    /**
     * Creates a new account after validating input and duplicate login rules.
     *
     * @return the created account with generated account number, or empty if creation failed
     */
    public Optional<Account> createAccount(String login, String pinCode, String holderName,
                                           BigDecimal balance, String status, String role) {
        validateAccountFields(login, pinCode, holderName, balance, status, role);
        String normalizedLogin = login.trim();

        if (accountRepository.findByLogin(normalizedLogin).isPresent()) {
            throw new IllegalArgumentException("Login already exists.");
        }

        Account account = new Account(
                0,
                normalizedLogin,
                pinCode.trim(),
                holderName.trim(),
                balance,
                status.trim().toUpperCase(),
                role.trim().toUpperCase()
        );
        return accountRepository.createAccount(account);
    }

    /**
     * Deletes an existing account.
     *
     * @param accountNumber account number to delete
     * @return true if deleted
     */
    public boolean deleteAccount(int accountNumber) {
        if (accountNumber <= 0) {
            throw new IllegalArgumentException("Account number must be positive.");
        }
        if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new IllegalArgumentException("Account not found.");
        }
        return accountRepository.deleteAccount(accountNumber);
    }

    /**
     * Updates account profile information.
     *
     * @return true if updated
     */
    public boolean updateAccount(int accountNumber, String login, String pinCode,
                                 String holderName, String status, String role) {
        if (accountNumber <= 0) {
            throw new IllegalArgumentException("Account number must be positive.");
        }
        validateAccountFields(login, pinCode, holderName, BigDecimal.ZERO, status, role);

        Account existing = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        String normalizedLogin = login.trim();
        Optional<Account> loginOwner = accountRepository.findByLogin(normalizedLogin);
        if (loginOwner.isPresent() && loginOwner.get().getAccountNumber() != accountNumber) {
            throw new IllegalArgumentException("Login already exists.");
        }

        Account updated = existing.withProfile(
                normalizedLogin,
                pinCode.trim(),
                holderName.trim(),
                status.trim().toUpperCase(),
                role.trim().toUpperCase()
        );
        return accountRepository.updateAccount(updated);
    }

    /**
     * Searches an account by account number.
     *
     * @param accountNumber account number to search
     * @return account if found
     */
    public Optional<Account> searchAccount(int accountNumber) {
        if (accountNumber <= 0) {
            throw new IllegalArgumentException("Account number must be positive.");
        }
        return accountRepository.findByAccountNumber(accountNumber);
    }

    private void validateAccountFields(String login, String pinCode, String holderName,
                                       BigDecimal balance, String status, String role) {
        if (isBlank(login)) {
            throw new IllegalArgumentException("Login cannot be empty.");
        }
        if (pinCode == null || !pinCode.trim().matches("\\d{5}")) {
            throw new IllegalArgumentException("PIN must be exactly 5 digits.");
        }
        if (isBlank(holderName)) {
            throw new IllegalArgumentException("Holder name cannot be empty.");
        }
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Status must be ACTIVE or DISABLED.");
        }
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Role must be CUSTOMER or ADMIN.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean isValidStatus(String status) {
        return status != null && ("ACTIVE".equalsIgnoreCase(status.trim())
                || "DISABLED".equalsIgnoreCase(status.trim()));
    }

    private boolean isValidRole(String role) {
        return role != null && ("CUSTOMER".equalsIgnoreCase(role.trim())
                || "ADMIN".equalsIgnoreCase(role.trim()));
    }
}
