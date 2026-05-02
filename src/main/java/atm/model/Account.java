package atm.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Immutable model object representing one ATM account.
 */
public final class Account {
    private final int accountNumber;
    private final String login;
    private final String pinCode;
    private final String holderName;
    private final BigDecimal balance;
    private final String status;
    private final String role;

    /**
     * Creates an account model.
     *
     * @param accountNumber unique account number
     * @param login account login name
     * @param pinCode five digit PIN code
     * @param holderName account holder name
     * @param balance current account balance
     * @param status account status, such as ACTIVE or DISABLED
     * @param role account role, such as CUSTOMER or ADMIN
     */
    public Account(int accountNumber, String login, String pinCode, String holderName,
                   BigDecimal balance, String status, String role) {
        this.accountNumber = accountNumber;
        this.login = Objects.requireNonNull(login, "login");
        this.pinCode = Objects.requireNonNull(pinCode, "pinCode");
        this.holderName = Objects.requireNonNull(holderName, "holderName");
        this.balance = Objects.requireNonNull(balance, "balance");
        this.status = Objects.requireNonNull(status, "status");
        this.role = Objects.requireNonNull(role, "role");
    }

    /**
     * Builds an Account from a JDBC ResultSet row.
     *
     * @param resultSet result set positioned on an account row
     * @return mapped account model
     * @throws SQLException if a result set value cannot be read
     */
    public static Account fromResultSet(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("account_number"),
                resultSet.getString("login"),
                resultSet.getString("pin_code"),
                resultSet.getString("holder_name"),
                resultSet.getBigDecimal("balance"),
                resultSet.getString("status"),
                resultSet.getString("role")
        );
    }

    /**
     * Returns a new account instance with an updated database-generated number.
     *
     * @param newAccountNumber generated account number
     * @return copied account with the new account number
     */
    public Account withAccountNumber(int newAccountNumber) {
        return new Account(newAccountNumber, login, pinCode, holderName, balance, status, role);
    }

    /**
     * Returns a new account instance with an updated balance.
     *
     * @param newBalance updated balance
     * @return copied account with the new balance
     */
    public Account withBalance(BigDecimal newBalance) {
        return new Account(accountNumber, login, pinCode, holderName, newBalance, status, role);
    }

    /**
     * Returns a new account instance with updated profile fields.
     *
     * @param newLogin updated login
     * @param newPinCode updated PIN
     * @param newHolderName updated holder name
     * @param newStatus updated status
     * @param newRole updated role
     * @return copied account with updated profile values
     */
    public Account withProfile(String newLogin, String newPinCode, String newHolderName,
                               String newStatus, String newRole) {
        return new Account(accountNumber, newLogin, newPinCode, newHolderName, balance, newStatus, newRole);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getLogin() {
        return login;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }
}
