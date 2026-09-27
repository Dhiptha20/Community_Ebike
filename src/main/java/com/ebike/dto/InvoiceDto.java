package com.ebike.dto;

import com.ebike.model.InvoiceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class InvoiceDto {

    private Long invoiceId;

    @NotNull(message = "Rider ID is required")
    private Long riderId;

    private Long tripId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private Double amount;

    private LocalDate dueDate;

    private InvoiceStatus status = InvoiceStatus.UNPAID;

    public InvoiceDto() {}

    public Long getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Long getRiderId() {
        return riderId;
    }
    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }
    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public InvoiceStatus getStatus() {
        return status;
    }
    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}
