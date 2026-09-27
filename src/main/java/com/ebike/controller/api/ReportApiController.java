package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.model.Account;
import com.ebike.model.AccountType;
import com.ebike.model.Budget;
import com.ebike.service.AccountingService;
import com.ebike.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/financial")
public class ReportApiController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private AccountingService accountingService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getFinancialSummary() {
        List<Account> accounts = accountingService.getAllAccounts();
        double totalAssets = accounts.stream().filter(a -> a.getAccountType() == AccountType.ASSET).mapToDouble(Account::getBalance).sum();
        double totalLiabilities = accounts.stream().filter(a -> a.getAccountType() == AccountType.LIABILITY).mapToDouble(Account::getBalance).sum();
        double totalEquity = accounts.stream().filter(a -> a.getAccountType() == AccountType.EQUITY).mapToDouble(Account::getBalance).sum();
        double totalRevenue = accounts.stream().filter(a -> a.getAccountType() == AccountType.REVENUE).mapToDouble(Account::getBalance).sum();
        double totalExpenses = accounts.stream().filter(a -> a.getAccountType() == AccountType.EXPENSE).mapToDouble(Account::getBalance).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAssets", totalAssets);
        summary.put("totalLiabilities", totalLiabilities);
        summary.put("totalEquity", totalEquity);
        summary.put("totalRevenue", totalRevenue);
        summary.put("totalExpenses", totalExpenses);
        summary.put("netSurplus", totalRevenue - totalExpenses);

        return ResponseEntity.ok(ApiResponseDto.success("Financial summary retrieved successfully", summary));
    }

    @GetMapping("/income-statement")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getIncomeStatement() {
        List<Account> accounts = accountingService.getAllAccounts();
        List<Account> revenues = accounts.stream().filter(a -> a.getAccountType() == AccountType.REVENUE).collect(Collectors.toList());
        List<Account> expenses = accounts.stream().filter(a -> a.getAccountType() == AccountType.EXPENSE).collect(Collectors.toList());

        double totalRevenue = revenues.stream().mapToDouble(Account::getBalance).sum();
        double totalExpense = expenses.stream().mapToDouble(Account::getBalance).sum();

        Map<String, Object> report = new HashMap<>();
        report.put("revenueAccounts", revenues);
        report.put("expenseAccounts", expenses);
        report.put("totalRevenue", totalRevenue);
        report.put("totalExpense", totalExpense);
        report.put("netOperatingIncome", totalRevenue - totalExpense);

        return ResponseEntity.ok(ApiResponseDto.success("Income statement generated successfully", report));
    }

    @GetMapping("/balance-sheet")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getBalanceSheet() {
        List<Account> accounts = accountingService.getAllAccounts();
        List<Account> assets = accounts.stream().filter(a -> a.getAccountType() == AccountType.ASSET).collect(Collectors.toList());
        List<Account> liabilities = accounts.stream().filter(a -> a.getAccountType() == AccountType.LIABILITY).collect(Collectors.toList());
        List<Account> equity = accounts.stream().filter(a -> a.getAccountType() == AccountType.EQUITY).collect(Collectors.toList());

        double totalAssets = assets.stream().mapToDouble(Account::getBalance).sum();
        double totalLiabilities = liabilities.stream().mapToDouble(Account::getBalance).sum();
        double totalEquity = equity.stream().mapToDouble(Account::getBalance).sum();

        Map<String, Object> report = new HashMap<>();
        report.put("assetAccounts", assets);
        report.put("liabilityAccounts", liabilities);
        report.put("equityAccounts", equity);
        report.put("totalAssets", totalAssets);
        report.put("totalLiabilities", totalLiabilities);
        report.put("totalEquity", totalEquity);

        return ResponseEntity.ok(ApiResponseDto.success("Balance sheet generated successfully", report));
    }

    @GetMapping("/budget-variance")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getBudgetVarianceReport() {
        List<Budget> budgets = budgetService.getAllBudgets();
        Double totalAllocated = budgetService.getTotalAllocated();
        Double totalSpend = budgetService.getTotalSpend();
        Double totalVariance = (totalAllocated != null ? totalAllocated : 0.0) - (totalSpend != null ? totalSpend : 0.0);

        Map<String, Object> report = new HashMap<>();
        report.put("totalAllocated", totalAllocated != null ? totalAllocated : 0.0);
        report.put("totalSpend", totalSpend != null ? totalSpend : 0.0);
        report.put("totalVariance", totalVariance);
        report.put("sectorBudgets", budgets);

        return ResponseEntity.ok(ApiResponseDto.success("Budget variance report generated", report));
    }
}
