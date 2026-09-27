package com.ebike.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PurchaseOrderDto {

    private Long purchaseOrderId;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private Double amount;

    private String category = "Fleet Maintenance";

    public PurchaseOrderDto() {}

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }
    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    public Long getVendorId() {
        return vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
