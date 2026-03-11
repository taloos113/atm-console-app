package atm.service;

import atm.model.Account;
import atm.repository.AccountRepository;

public class AuthService {
    private final AccountRepository accountRepository = new AccountRepository();

    public Account login(String login, String pinCode) {
        if (login == null || login.isBlank()) {
            System.out.println("Login cannot be empty.");
            return null;
        }

        if (pinCode == null || !pinCode.matches("\\d{5}")) {
            System.out.println("PIN must be exactly 5 digits.");
            return null;
        }

        return accountRepository.findByLoginAndPin(login, pinCode);
    }
}