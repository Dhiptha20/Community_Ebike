package com.ebike.controller;

import com.ebike.dto.BudgetDto;
import com.ebike.model.Budget;
import com.ebike.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public String listBudgets(Model model) {
        List<Budget> budgets = budgetService.getAllBudgets();
        Double totalAllocated = budgetService.getTotalAllocated();
        Double totalSpend = budgetService.getTotalSpend();
        Double totalVariance = (totalAllocated != null ? totalAllocated : 0.0) - (totalSpend != null ? totalSpend : 0.0);

        model.addAttribute("budgets", budgets);
        model.addAttribute("totalAllocated", totalAllocated != null ? totalAllocated : 0.0);
        model.addAttribute("totalSpend", totalSpend != null ? totalSpend : 0.0);
        model.addAttribute("totalVariance", totalVariance);
        model.addAttribute("newBudget", new BudgetDto());

        return "budgets";
    }

    @PostMapping("/save")
    public String saveBudget(@Valid @ModelAttribute("newBudget") BudgetDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/budgets";
        }
        try {
            budgetService.saveBudget(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Sector budget saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/budgets";
    }
}
