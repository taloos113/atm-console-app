package atm.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import atm.model.Account;
import atm.repository.AccountRepository;
import atm.repository.TransactionRepository;

public class CustomerService {
    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public Account refreshAccount(Account account) {
        return accountRepository.findByAccountNumber(account.getAccountNumber());
    }

    public boolean deposit(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Deposit amount must be greater than 0.");
            return false;
        }

        BigDecimal newBalance = account.getBalance().add(amount);

        boolean updated = accountRepository.updateBalance(account.getAccountNumber(), newBalance);
        if (!updated) {
            System.out.println("Failed to update account balance.");
            return false;
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

        account.setBalance(newBalance);

        printTransactionReceipt(account, "DEPOSIT", amount, newBalance);
        return true;
    }

    public boolean withdraw(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Withdrawal amount must be greater than 0.");
            return false;
        }

        if (account.getBalance().compareTo(amount) < 0) {
            System.out.println("Insufficient balance.");
            return false;
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);

        boolean updated = accountRepository.updateBalance(account.getAccountNumber(), newBalance);
        if (!updated) {
            System.out.println("Failed to update account balance.");
            return false;
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

        account.setBalance(newBalance);

        printTransactionReceipt(account, "WITHDRAW", amount, newBalance);
        return true;
    }

    public void displayBalance(Account account) {
        Account latestAccount = refreshAccount(account);
        if (latestAccount != null) {
            account.setBalance(latestAccount.getBalance());
        }
        System.out.println("Current balance: $" + account.getBalance());
    }

    private void printTransactionReceipt(Account account, String type,
                                         BigDecimal amount, BigDecimal balanceAfter) {
        System.out.println("\n========== Transaction Receipt ==========");
        System.out.println("Account Number : " + account.getAccountNumber());
        System.out.println("Account Holder : " + account.getHolderName());
        System.out.println("Transaction    : " + type);
        System.out.println("Amount         : $" + amount);
        System.out.println("Date/Time      : " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Balance After  : $" + balanceAfter);
        System.out.println("=========================================\n");
    }
}