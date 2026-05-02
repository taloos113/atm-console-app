package atm.repository;

import atm.db.DBConnection;
import atm.model.Account;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

/**
 * JDBC implementation of account SQL operations.
 */
public class AccountRepository implements IAccountRepository {
    @Override
    public Optional<Account> findByLoginAndPin(String login, String pinCode) {
        String sql = "SELECT * FROM accounts WHERE login = ? AND pin_code = ? AND status = 'ACTIVE'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, pinCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Account.fromResultSet(resultSet));
                }
            }
        } catch (Exception exception) {
            System.out.println("Error finding account: " + exception.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByAccountNumber(int accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Account.fromResultSet(resultSet));
                }
            }
        } catch (Exception exception) {
            System.out.println("Error finding account by number: " + exception.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        String sql = "SELECT * FROM accounts WHERE login = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Account.fromResultSet(resultSet));
                }
            }
        } catch (Exception exception) {
            System.out.println("Error finding account by login: " + exception.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> createAccount(Account account) {
        String sql = "INSERT INTO accounts (login, pin_code, holder_name, balance, status, role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getLogin());
            statement.setString(2, account.getPinCode());
            statement.setString(3, account.getHolderName());
            statement.setBigDecimal(4, account.getBalance());
            statement.setString(5, account.getStatus());
            statement.setString(6, account.getRole());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return Optional.empty();
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(account.withAccountNumber(generatedKeys.getInt(1)));
                }
            }
        } catch (Exception exception) {
            System.out.println("Error creating account: " + exception.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteAccount(int accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountNumber);
            return statement.executeUpdate() > 0;
        } catch (Exception exception) {
            System.out.println("Error deleting account: " + exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts SET holder_name = ?, login = ?, pin_code = ?, status = ?, role = ? "
                + "WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getHolderName());
            statement.setString(2, account.getLogin());
            statement.setString(3, account.getPinCode());
            statement.setString(4, account.getStatus());
            statement.setString(5, account.getRole());
            statement.setInt(6, account.getAccountNumber());
            return statement.executeUpdate() > 0;
        } catch (Exception exception) {
            System.out.println("Error updating account: " + exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBalance(int accountNumber, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, newBalance);
            statement.setInt(2, accountNumber);
            return statement.executeUpdate() > 0;
        } catch (Exception exception) {
            System.out.println("Error updating balance: " + exception.getMessage());
            return false;
        }
    }
}
