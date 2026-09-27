package com.ebike.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BudgetDto {

    private Long budgetId;

    @NotBlank(message = "Sector is required")
    private String sector;

    @NotNull(message = "Allocated fund is required")
    @DecimalMin(value = "0.0", message = "Allocated fund cannot be negative")
    private Double allocatedFund;

    private Double actualSpend = 0.0;

    private String fiscalYear = "2024-2025";

    public BudgetDto() {}

    public Long getBudgetId() {
        return budgetId;
    }
    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public Double getAllocatedFund() {
        return allocatedFund;
    }
    public void setAllocatedFund(Double allocatedFund) {
        this.allocatedFund = allocatedFund;
    }
    public Double getActualSpend() {
        return actualSpend;
    }
    public void setActualSpend(Double actualSpend) {
        this.actualSpend = actualSpend;
    }
    public String getFiscalYear() {
        return fiscalYear;
    }
    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
}
