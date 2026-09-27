package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bikes")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bikeId;

    @NotBlank(message = "Bike code is required")
    @Column(nullable = false, unique = true, length = 30)
    private String bikeCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DockingStation dockingStation;

    @NotNull(message = "Battery level is required")
    @Min(value = 0, message = "Battery level must be at least 0%")
    @Max(value = 100, message = "Battery level cannot exceed 100%")
    @Column(nullable = false)
    private Integer batteryLevel = 100;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private BikeStatus status = BikeStatus.AVAILABLE;

    @Column(name = "gps_latitude")
    private Double gpsLatitude = 12.9716; // Default to realistic coordinates (e.g. Bangalore)

    @Column(name = "gps_longitude")
    private Double gpsLongitude = 77.5946;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Bike() {}

    public Bike(String bikeCode, DockingStation dockingStation, Integer batteryLevel, BikeStatus status, Double gpsLatitude, Double gpsLongitude) {
        this.bikeCode = bikeCode;
        this.dockingStation = dockingStation;
        this.batteryLevel = batteryLevel != null ? batteryLevel : 100;
        this.status = status != null ? status : BikeStatus.AVAILABLE;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
    }

    public boolean isLowBattery() {
        return this.batteryLevel != null && this.batteryLevel < 20;
    }

    // Getters and Setters
    public Long getBikeId() {
        return bikeId;
    }
    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }
    public String getBikeCode() {
        return bikeCode;
    }
    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }
    public DockingStation getDockingStation() {
        return dockingStation;
    }
    public void setDockingStation(DockingStation dockingStation) {
        this.dockingStation = dockingStation;
    }
    public Integer getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    public BikeStatus getStatus() {
        return status;
    }
    public void setStatus(BikeStatus status) {
        this.status = status;
    }
    public Double getGpsLatitude() {
        return gpsLatitude;
    }
    public void setGpsLatitude(Double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }
    public Double getGpsLongitude() {
        return gpsLongitude;
    }
    public void setGpsLongitude(Double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
