package atm.repository;

import atm.model.Account;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Repository contract for account persistence operations.
 */
public interface IAccountRepository {
    Optional<Account> findByLoginAndPin(String login, String pinCode);

    Optional<Account> findByAccountNumber(int accountNumber);

    Optional<Account> findByLogin(String login);

    Optional<Account> createAccount(Account account);

    boolean deleteAccount(int accountNumber);

    boolean updateAccount(Account account);

    boolean updateBalance(int accountNumber, BigDecimal newBalance);
}
