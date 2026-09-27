package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rider_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rider rider;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Trip trip;

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.dueDate == null) {
            this.dueDate = LocalDate.now().plusDays(7);
        }
    }

    public Invoice() {}

    public Invoice(Rider rider, Trip trip, Double amount, LocalDate dueDate, InvoiceStatus status) {
        this.rider = rider;
        this.trip = trip;
        this.amount = amount;
        this.dueDate = dueDate != null ? dueDate : LocalDate.now().plusDays(7);
        this.status = status != null ? status : InvoiceStatus.UNPAID;
    }

    // Getters and Setters
    public Long getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Rider getRider() {
        return rider;
    }
    public void setRider(Rider rider) {
        this.rider = rider;
    }
    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
