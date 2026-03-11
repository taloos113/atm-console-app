package atm.model;

import java.math.BigDecimal;

public class Account {
    private int accountNumber;
    private String login;
    private String pinCode;
    private String holderName;
    private BigDecimal balance;
    private String status;
    private String role;

    public Account() {
    }

    public Account(int accountNumber, String login, String pinCode, String holderName,
                   BigDecimal balance, String status, String role) {
        this.accountNumber = accountNumber;
        this.login = login;
        this.pinCode = pinCode;
        this.holderName = holderName;
        this.balance = balance;
        this.status = status;
        this.role = role;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}