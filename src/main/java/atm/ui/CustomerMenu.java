package atm.ui;

import atm.model.Account;
import atm.service.CustomerService;
import atm.util.InputUtil;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Console menu for customer ATM operations.
 */
public class CustomerMenu {
    private final Scanner scanner;
    private final CustomerService customerService;

    /**
     * Creates a customer menu with its service dependency.
     *
     * @param customerService customer service dependency
     */
    public CustomerMenu(CustomerService customerService) {
        this.scanner = new Scanner(System.in);
        this.customerService = customerService;
    }

    /**
     * Starts the customer menu loop.
     *
     * @param account authenticated customer account
     */
    public void start(Account account) {
        Account currentAccount = account;
        boolean running = true;
        while (running) {
            System.out.println("\n=================================");
            System.out.println("Customer Menu");
            System.out.println("Welcome, " + currentAccount.getHolderName());
            System.out.println("1. Withdraw Cash");
            System.out.println("2. Deposit Cash");
            System.out.println("3. Display Balance");
            System.out.println("4. Logout");
            System.out.print("Please choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> currentAccount = handleWithdraw(currentAccount);
                case "2" -> currentAccount = handleDeposit(currentAccount);
                case "3" -> currentAccount = handleDisplayBalance(currentAccount);
                case "4" -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private Account handleDeposit(Account account) {
        try {
            BigDecimal amount = InputUtil.readBigDecimal(scanner, "Enter deposit amount: ");
            return customerService.deposit(account, amount);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            System.out.println(exception.getMessage());
            return account;
        }
    }

    private Account handleWithdraw(Account account) {
        try {
            BigDecimal amount = InputUtil.readBigDecimal(scanner, "Enter withdrawal amount: ");
            return customerService.withdraw(account, amount);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            System.out.println(exception.getMessage());
            return account;
        }
    }

    private Account handleDisplayBalance(Account account) {
        try {
            return customerService.displayBalance(account);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            return account;
        }
    }
}
