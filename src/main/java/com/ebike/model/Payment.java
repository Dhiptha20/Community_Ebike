package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Invoice invoice;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rider_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rider rider;

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.SUCCESS;

    @Column(length = 64)
    private String transactionRef;

    @PrePersist
    protected void onCreate() {
        if (this.paymentDate == null) {
            this.paymentDate = LocalDateTime.now();
        }
        if (this.transactionRef == null) {
            this.transactionRef = "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000);
        }
    }

    public Payment() {}

    public Payment(Invoice invoice, Rider rider, Double amount, PaymentMethod paymentMethod, PaymentStatus status) {
        this.invoice = invoice;
        this.rider = rider;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status != null ? status : PaymentStatus.SUCCESS;
        this.paymentDate = LocalDateTime.now();
        this.transactionRef = "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000);
    }

    // Getters and Setters
    public Long getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public Invoice getInvoice() {
        return invoice;
    }
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    public Rider getRider() {
        return rider;
    }
    public void setRider(Rider rider) {
        this.rider = rider;
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
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    public PaymentStatus getStatus() {
        return status;
    }
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    public String getTransactionRef() {
        return transactionRef;
    }
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}
