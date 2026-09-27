package com.ebike.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_tariffs")
public class RentalTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tariffId;

    @NotBlank(message = "Tariff name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Base fare is required")
    @DecimalMin(value = "0.0", message = "Base fare cannot be negative")
    @Column(nullable = false)
    private Double baseFare = 10.0;

    @NotNull(message = "Per minute rate is required")
    @DecimalMin(value = "0.0", message = "Per minute rate cannot be negative")
    @Column(nullable = false)
    private Double perMinuteRate = 2.0;

    @NotNull(message = "Per kilometer rate is required")
    @DecimalMin(value = "0.0", message = "Per kilometer rate cannot be negative")
    @Column(nullable = false)
    private Double perKmRate = 5.0;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public RentalTariff() {}

    public RentalTariff(String name, Double baseFare, Double perMinuteRate, Double perKmRate, boolean active) {
        this.name = name;
        this.baseFare = baseFare;
        this.perMinuteRate = perMinuteRate;
        this.perKmRate = perKmRate;
        this.active = active;
    }

    public Double calculateFare(long durationMinutes, double distanceKm) {
        double calculated = this.baseFare + (durationMinutes * this.perMinuteRate) + (distanceKm * this.perKmRate);
        return Math.round(calculated * 100.0) / 100.0; // round to 2 decimals
    }

    // Getters and Setters
    public Long getTariffId() {
        return tariffId;
    }
    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getBaseFare() {
        return baseFare;
    }
    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }
    public Double getPerMinuteRate() {
        return perMinuteRate;
    }
    public void setPerMinuteRate(Double perMinuteRate) {
        this.perMinuteRate = perMinuteRate;
    }
    public Double getPerKmRate() {
        return perKmRate;
    }
    public void setPerKmRate(Double perKmRate) {
        this.perKmRate = perKmRate;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
