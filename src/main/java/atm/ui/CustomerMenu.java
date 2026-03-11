package atm.ui;

import java.math.BigDecimal;
import java.util.Scanner;

import atm.model.Account;
import atm.service.CustomerService;
import atm.util.InputUtil;

public class CustomerMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final CustomerService customerService = new CustomerService();

    public void start(Account account) {
        boolean running = true;

        while (running) {
            System.out.println("\n=================================");
            System.out.println("Customer Menu");
            System.out.println("Welcome, " + account.getHolderName());
            System.out.println("1. Withdraw Cash");
            System.out.println("2. Deposit Cash");
            System.out.println("3. Display Balance");
            System.out.println("4. Logout");
            System.out.print("Please choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleWithdraw(account);
                    break;
                case "2":
                    handleDeposit(account);
                    break;
                case "3":
                    customerService.displayBalance(account);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleDeposit(Account account) {
        BigDecimal amount = InputUtil.readBigDecimal(scanner, "Enter deposit amount: ");
        customerService.deposit(account, amount);
    }

    private void handleWithdraw(Account account) {
        BigDecimal amount = InputUtil.readBigDecimal(scanner, "Enter withdrawal amount: ");
        customerService.withdraw(account, amount);
    }
}