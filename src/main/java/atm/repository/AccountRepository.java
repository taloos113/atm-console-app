package atm.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import atm.db.DBConnection;
import atm.model.Account;

public class AccountRepository {

    public Account findByLoginAndPin(String login, String pinCode) {
        String sql = "SELECT * FROM accounts WHERE login = ? AND pin_code = ? AND status = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, pinCode);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }

        } catch (Exception e) {
            System.out.println("Error finding account: " + e.getMessage());
        }

        return null;
    }

    public Account findByAccountNumber(int accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }

        } catch (Exception e) {
            System.out.println("Error finding account by number: " + e.getMessage());
        }

        return null;
    }

    public Account findByLogin(String login) {
        String sql = "SELECT * FROM accounts WHERE login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }

        } catch (Exception e) {
            System.out.println("Error finding account by login: " + e.getMessage());
        }

        return null;
    }

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (login, pin_code, holder_name, balance, status, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getLogin());
            stmt.setString(2, account.getPinCode());
            stmt.setString(3, account.getHolderName());
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, account.getStatus());
            stmt.setString(6, account.getRole());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAccount(int accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountNumber);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts SET holder_name = ?, login = ?, pin_code = ?, status = ?, role = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getHolderName());
            stmt.setString(2, account.getLogin());
            stmt.setString(3, account.getPinCode());
            stmt.setString(4, account.getStatus());
            stmt.setString(5, account.getRole());
            stmt.setInt(6, account.getAccountNumber());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error updating account: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBalance(int accountNumber, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, accountNumber);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws Exception {
        Account account = new Account();
        account.setAccountNumber(rs.getInt("account_number"));
        account.setLogin(rs.getString("login"));
        account.setPinCode(rs.getString("pin_code"));
        account.setHolderName(rs.getString("holder_name"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setStatus(rs.getString("status"));
        account.setRole(rs.getString("role"));
        return account;
    }
}