package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderId;

    @Column(nullable = false, unique = true, length = 40)
    private String poNumber;

    @NotNull(message = "Vendor is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vendor vendor;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 255)
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private POStatus status = POStatus.CREATED;

    @Column(length = 60)
    private String category = "Fleet Maintenance";

    @PrePersist
    protected void onCreate() {
        if (this.orderDate == null) {
            this.orderDate = LocalDate.now();
        }
        if (this.poNumber == null) {
            this.poNumber = "PO-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000);
        }
    }

    public PurchaseOrder() {}

    public PurchaseOrder(Vendor vendor, String description, Double amount, LocalDate orderDate, POStatus status, String category) {
        this.vendor = vendor;
        this.description = description;
        this.amount = amount;
        this.orderDate = orderDate != null ? orderDate : LocalDate.now();
        this.status = status != null ? status : POStatus.CREATED;
        this.category = category != null ? category : "Fleet Maintenance";
        this.poNumber = "PO-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000);
    }

    // Getters and Setters
    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }
    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    public String getPoNumber() {
        return poNumber;
    }
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
    public Vendor getVendor() {
        return vendor;
    }
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
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
    public LocalDate getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    public POStatus getStatus() {
        return status;
    }
    public void setStatus(POStatus status) {
        this.status = status;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
