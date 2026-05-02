package atm.service;

import atm.model.Account;
import atm.repository.IAccountRepository;
import atm.repository.ITransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Handles customer banking operations.
 */
public class CustomerService {
    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;

    /**
     * Creates a customer service using repository interfaces.
     *
     * @param accountRepository account repository dependency
     * @param transactionRepository transaction repository dependency
     */
    public CustomerService(IAccountRepository accountRepository, ITransactionRepository transactionRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
        this.transactionRepository = Objects.requireNonNull(transactionRepository, "transactionRepository");
    }

    /**
     * Gets the latest account data from storage.
     *
     * @param account current account
     * @return refreshed account
     */
    public Account refreshAccount(Account account) {
        requireAccount(account);
        return accountRepository.findByAccountNumber(account.getAccountNumber()).orElse(account);
    }

    /**
     * Deposits cash into an account.
     *
     * @param account customer account
     * @param amount deposit amount
     * @return updated immutable account copy
     */
    public Account deposit(Account account, BigDecimal amount) {
        requireAccount(account);
        requirePositiveAmount(amount, "Deposit amount must be greater than 0.");

        BigDecimal newBalance = account.getBalance().add(amount);
        if (!accountRepository.updateBalance(account.getAccountNumber(), newBalance)) {
            throw new IllegalStateException("Failed to update account balance.");
        }

        boolean recorded = transactionRepository.addTransaction(
                account.getAccountNumber(),
                "DEPOSIT",
                amount,
                newBalance,
                "Cash deposit"
        );
        if (!recorded) {
            System.out.println("Warning: balance updated, but transaction record failed.");
        }

        Account updatedAccount = account.withBalance(newBalance);
        printTransactionReceipt(updatedAccount, "DEPOSIT", amount, newBalance);
        return updatedAccount;
    }

    /**
     * Withdraws cash from an account.
     *
     * @param account customer account
     * @param amount withdrawal amount
     * @return updated immutable account copy
     */
    public Account withdraw(Account account, BigDecimal amount) {
        requireAccount(account);
        requirePositiveAmount(amount, "Withdrawal amount must be greater than 0.");

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (!accountRepository.updateBalance(account.getAccountNumber(), newBalance)) {
            throw new IllegalStateException("Failed to update account balance.");
        }

        boolean recorded = transactionRepository.addTransaction(
                account.getAccountNumber(),
                "WITHDRAW",
                amount,
                newBalance,
                "Cash withdrawal"
        );
        if (!recorded) {
            System.out.println("Warning: balance updated, but transaction record failed.");
        }

        Account updatedAccount = account.withBalance(newBalance);
        printTransactionReceipt(updatedAccount, "WITHDRAW", amount, newBalance);
        return updatedAccount;
    }

    /**
     * Prints the current balance and returns the latest account version.
     *
     * @param account customer account
     * @return refreshed account
     */
    public Account displayBalance(Account account) {
        Account latestAccount = refreshAccount(account);
        System.out.println("Current balance: $" + latestAccount.getBalance());
        return latestAccount;
    }

    private void requireAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is required.");
        }
    }

    private void requirePositiveAmount(BigDecimal amount, String message) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private void printTransactionReceipt(Account account, String type, BigDecimal amount, BigDecimal balanceAfter) {
        System.out.println("\n========== Transaction Receipt ==========");
        System.out.println("Account Number : " + account.getAccountNumber());
        System.out.println("Account Holder : " + account.getHolderName());
        System.out.println("Transaction : " + type);
        System.out.println("Amount : $" + amount);
        System.out.println("Date/Time : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Balance After : $" + balanceAfter);
        System.out.println("=========================================\n");
    }
}
