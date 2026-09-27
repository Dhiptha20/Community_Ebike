package com.ebike.service;

import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.Account;
import com.ebike.model.AccountType;
import com.ebike.model.JournalEntry;
import com.ebike.repository.AccountRepository;
import com.ebike.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountingService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAllByOrderByTransactionDateDesc();
    }

    public Account getAccountByCode(String code) {
        return accountRepository.findByAccountCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with code: " + code));
    }

    @Transactional
    public JournalEntry recordTransaction(String debitAccountCode, String creditAccountCode, Double amount, String description, String reference) {
        Account debitAccount = getAccountByCode(debitAccountCode);
        Account creditAccount = getAccountByCode(creditAccountCode);

        // Update balances:
        // For Assets & Expenses: Debit increases balance (+), Credit decreases balance (-)
        // For Liabilities, Equity & Revenue: Credit increases balance (+), Debit decreases balance (-)
        updateBalance(debitAccount, amount, true);
        updateBalance(creditAccount, amount, false);

        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);

        JournalEntry entry = new JournalEntry();
        entry.setTransactionDate(LocalDateTime.now());
        entry.setDescription(description);
        entry.setReference(reference);
        entry.setDebitAccount(debitAccount);
        entry.setCreditAccount(creditAccount);
        entry.setAmount(amount);

        return journalEntryRepository.save(entry);
    }

    private void updateBalance(Account account, Double amount, boolean isDebit) {
        AccountType type = account.getAccountType();
        if (type == AccountType.ASSET || type == AccountType.EXPENSE) {
            if (isDebit) {
                account.setBalance(account.getBalance() + amount);
            } else {
                account.setBalance(account.getBalance() - amount);
            }
        } else { // LIABILITY, EQUITY, REVENUE
            if (isDebit) {
                account.setBalance(account.getBalance() - amount);
            } else {
                account.setBalance(account.getBalance() + amount);
            }
        }
    }
}
