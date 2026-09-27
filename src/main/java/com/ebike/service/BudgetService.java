package com.ebike.service;

import com.ebike.dto.BudgetDto;
import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.Budget;
import com.ebike.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with ID: " + id));
    }

    public Optional<Budget> getBudgetBySector(String sector) {
        return budgetRepository.findBySector(sector);
    }

    @Transactional
    public Budget saveBudget(BudgetDto dto) {
        Budget budget;
        if (dto.getBudgetId() != null) {
            budget = getBudgetById(dto.getBudgetId());
        } else {
            budget = budgetRepository.findBySector(dto.getSector().trim()).orElse(new Budget());
        }

        budget.setSector(dto.getSector().trim());
        budget.setAllocatedFund(dto.getAllocatedFund());
        if (dto.getActualSpend() != null) {
            budget.setActualSpend(dto.getActualSpend());
        }
        if (dto.getFiscalYear() != null) {
            budget.setFiscalYear(dto.getFiscalYear());
        }
        budget.calculateVariance();

        return budgetRepository.save(budget);
    }

    @Transactional
    public void recordSpend(String sector, Double amount) {
        Optional<Budget> optBudget = budgetRepository.findBySector(sector);
        if (optBudget.isPresent()) {
            Budget budget = optBudget.get();
            budget.setActualSpend(budget.getActualSpend() + amount);
            budget.calculateVariance();
            budgetRepository.save(budget);
        }
    }

    public Double getTotalAllocated() {
        return budgetRepository.getTotalAllocatedBudget();
    }

    public Double getTotalSpend() {
        return budgetRepository.getTotalActualSpend();
    }
}
