package atm.service;

import atm.model.Account;
import atm.repository.IAccountRepository;
import java.util.Objects;

/**
 * Handles login validation and authentication.
 */
public class AuthService {
    private final IAccountRepository accountRepository;

    /**
     * Creates an authentication service using an account repository interface.
     *
     * @param accountRepository account repository dependency
     */
    public AuthService(IAccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
    }

    /**
     * Attempts to log in with a login name and 5-digit PIN.
     *
     * @param login login name
     * @param pinCode five digit PIN
     * @return authenticated account, or null if invalid
     */
    public Account login(String login, String pinCode) {
        if (isBlank(login) || !isValidPin(pinCode)) {
            return null;
        }
        return accountRepository.findByLoginAndPin(login.trim(), pinCode.trim()).orElse(null);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean isValidPin(String pinCode) {
        return pinCode != null && pinCode.trim().matches("\\d{5}");
    }
}
