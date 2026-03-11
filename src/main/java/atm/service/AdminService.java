package atm.service;

import java.math.BigDecimal;

import atm.model.Account;
import atm.repository.AccountRepository;

public class AdminService {
    private final AccountRepository accountRepository = new AccountRepository();

    public boolean createAccount(String login, String pinCode, String holderName,
                                 BigDecimal balance, String status, String role) {
        if (login == null || login.isBlank()) {
            System.out.println("Login cannot be empty.");
            return false;
        }

        if (accountRepository.findByLogin(login) != null) {
            System.out.println("Login already exists.");
            return false;
        }

        if (pinCode == null || !pinCode.matches("\\d{5}")) {
            System.out.println("PIN must be exactly 5 digits.");
            return false;
        }

        if (holderName == null || holderName.isBlank()) {
            System.out.println("Holder name cannot be empty.");
            return false;
        }

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Balance cannot be negative.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(status) && !"DISABLED".equalsIgnoreCase(status)) {
            System.out.println("Status must be ACTIVE or DISABLED.");
            return false;
        }

        if (!"CUSTOMER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            System.out.println("Role must be CUSTOMER or ADMIN.");
            return false;
        }

        Account account = new Account();
        account.setLogin(login);
        account.setPinCode(pinCode);
        account.setHolderName(holderName);
        account.setBalance(balance);
        account.setStatus(status.toUpperCase());
        account.setRole(role.toUpperCase());

        return accountRepository.createAccount(account);
    }

    public boolean deleteAccount(int accountNumber) {
        Account existing = accountRepository.findByAccountNumber(accountNumber);
        if (existing == null) {
            System.out.println("Account not found.");
            return false;
        }

        return accountRepository.deleteAccount(accountNumber);
    }

    public boolean updateAccount(int accountNumber, String login, String pinCode,
                                 String holderName, String status, String role) {
        Account existing = accountRepository.findByAccountNumber(accountNumber);
        if (existing == null) {
            System.out.println("Account not found.");
            return false;
        }

        if (login == null || login.isBlank()) {
            System.out.println("Login cannot be empty.");
            return false;
        }

        Account loginOwner = accountRepository.findByLogin(login);
        if (loginOwner != null && loginOwner.getAccountNumber() != accountNumber) {
            System.out.println("Login already exists.");
            return false;
        }

        if (pinCode == null || !pinCode.matches("\\d{5}")) {
            System.out.println("PIN must be exactly 5 digits.");
            return false;
        }

        if (holderName == null || holderName.isBlank()) {
            System.out.println("Holder name cannot be empty.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(status) && !"DISABLED".equalsIgnoreCase(status)) {
            System.out.println("Status must be ACTIVE or DISABLED.");
            return false;
        }

        if (!"CUSTOMER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            System.out.println("Role must be CUSTOMER or ADMIN.");
            return false;
        }

        existing.setLogin(login);
        existing.setPinCode(pinCode);
        existing.setHolderName(holderName);
        existing.setStatus(status.toUpperCase());
        existing.setRole(role.toUpperCase());

        return accountRepository.updateAccount(existing);
    }

    public Account searchAccount(int accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}