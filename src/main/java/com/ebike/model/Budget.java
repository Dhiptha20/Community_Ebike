package com.ebike.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @NotBlank(message = "Sector is required")
    @Column(nullable = false, length = 100)
    private String sector;

    @NotNull(message = "Allocated fund is required")
    @DecimalMin(value = "0.0", message = "Allocated fund cannot be negative")
    @Column(nullable = false)
    private Double allocatedFund;

    @NotNull
    @Column(nullable = false)
    private Double actualSpend = 0.0;

    @Column(nullable = false)
    private Double variance = 0.0;

    @Column(length = 20)
    private String fiscalYear = "2024-2025";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    public void calculateVariance() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.actualSpend == null) {
            this.actualSpend = 0.0;
        }
        if (this.allocatedFund == null) {
            this.allocatedFund = 0.0;
        }
        this.variance = Math.round((this.allocatedFund - this.actualSpend) * 100.0) / 100.0;
    }

    public Budget() {}

    public Budget(String sector, Double allocatedFund, Double actualSpend, String fiscalYear) {
        this.sector = sector;
        this.allocatedFund = allocatedFund;
        this.actualSpend = actualSpend != null ? actualSpend : 0.0;
        this.fiscalYear = fiscalYear != null ? fiscalYear : "2024-2025";
        calculateVariance();
    }

    public double getUtilizationPercentage() {
        if (allocatedFund == null || allocatedFund == 0.0) return 0.0;
        return Math.min(100.0, Math.round(((actualSpend / allocatedFund) * 100.0) * 10.0) / 10.0);
    }

    // Getters and Setters
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
        calculateVariance();
    }
    public Double getActualSpend() {
        return actualSpend;
    }
    public void setActualSpend(Double actualSpend) {
        this.actualSpend = actualSpend;
        calculateVariance();
    }
    public Double getVariance() {
        return variance;
    }
    public void setVariance(Double variance) {
        this.variance = variance;
    }
    public String getFiscalYear() {
        return fiscalYear;
    }
    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
