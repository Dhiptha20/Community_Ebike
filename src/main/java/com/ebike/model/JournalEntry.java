package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long journalEntryId;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 255)
    private String description;

    @Column(length = 80)
    private String reference;

    @NotNull(message = "Debit account is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account debitAccount;

    @NotNull(message = "Credit account is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account creditAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Column(nullable = false)
    private Double amount;

    @PrePersist
    protected void onCreate() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }

    public JournalEntry() {}

    public JournalEntry(LocalDateTime transactionDate, String description, String reference, Account debitAccount, Account creditAccount, Double amount) {
        this.transactionDate = transactionDate != null ? transactionDate : LocalDateTime.now();
        this.description = description;
        this.reference = reference;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getJournalEntryId() {
        return journalEntryId;
    }
    public void setJournalEntryId(Long journalEntryId) {
        this.journalEntryId = journalEntryId;
    }
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public Account getDebitAccount() {
        return debitAccount;
    }
    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }
    public Account getCreditAccount() {
        return creditAccount;
    }
    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
