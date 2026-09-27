package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.BudgetDto;
import com.ebike.model.Budget;
import com.ebike.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetApiController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Budget>>> getAllBudgets() {
        List<Budget> budgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(ApiResponseDto.success("Budgets retrieved successfully", budgets));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponseDto<java.util.Map<String, Object>>> getBudgetSummary() {
        Double totalAllocated = budgetService.getTotalAllocated();
        Double totalSpend = budgetService.getTotalSpend();
        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalAllocated", totalAllocated != null ? totalAllocated : 0.0);
        summary.put("totalSpend", totalSpend != null ? totalSpend : 0.0);
        summary.put("totalVariance", (totalAllocated != null ? totalAllocated : 0.0) - (totalSpend != null ? totalSpend : 0.0));
        return ResponseEntity.ok(ApiResponseDto.success("Budget summary retrieved", summary));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Budget>> saveBudget(@Valid @RequestBody BudgetDto dto) {
        Budget saved = budgetService.saveBudget(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Budget saved successfully", saved), HttpStatus.CREATED);
    }
}
