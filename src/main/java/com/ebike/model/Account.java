package com.ebike.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotBlank(message = "Account code is required")
    @Column(nullable = false, unique = true, length = 20)
    private String accountCode;

    @NotBlank(message = "Account name is required")
    @Column(nullable = false, length = 100)
    private String accountName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @Column(nullable = false)
    private Double balance = 0.0;

    public Account() {}

    public Account(String accountCode, String accountName, AccountType accountType, Double balance) {
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance != null ? balance : 0.0;
    }

    // Getters and Setters
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getAccountCode() {
        return accountCode;
    }
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
