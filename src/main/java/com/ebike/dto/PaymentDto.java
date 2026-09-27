package com.ebike.dto;

import com.ebike.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class PaymentDto {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    private Long riderId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public PaymentDto() {}

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
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
