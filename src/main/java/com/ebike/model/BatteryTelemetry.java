package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "battery_telemetry")
public class BatteryTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telemetryId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bike_id", nullable = false)
    @JsonIgnoreProperties({"dockingStation", "hibernateLazyInitializer", "handler"})
    private Bike bike;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer batteryLevel;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime readingTime;

    @Column(nullable = false)
    private boolean lowBatteryAlert;

    @PrePersist
    protected void onCreate() {
        if (this.readingTime == null) {
            this.readingTime = LocalDateTime.now();
        }
        if (this.batteryLevel != null && this.batteryLevel < 20) {
            this.lowBatteryAlert = true;
        }
    }

    public BatteryTelemetry() {}

    public BatteryTelemetry(Bike bike, Integer batteryLevel, Double latitude, Double longitude, LocalDateTime readingTime) {
        this.bike = bike;
        this.batteryLevel = batteryLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.readingTime = readingTime != null ? readingTime : LocalDateTime.now();
        this.lowBatteryAlert = batteryLevel != null && batteryLevel < 20;
    }

    // Getters and Setters
    public Long getTelemetryId() {
        return telemetryId;
    }
    public void setTelemetryId(Long telemetryId) {
        this.telemetryId = telemetryId;
    }
    public Bike getBike() {
        return bike;
    }
    public void setBike(Bike bike) {
        this.bike = bike;
    }
    public Integer getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
        this.lowBatteryAlert = batteryLevel != null && batteryLevel < 20;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public LocalDateTime getReadingTime() {
        return readingTime;
    }
    public void setReadingTime(LocalDateTime readingTime) {
        this.readingTime = readingTime;
    }
    public boolean isLowBatteryAlert() {
        return lowBatteryAlert;
    }
    public void setLowBatteryAlert(boolean lowBatteryAlert) {
        this.lowBatteryAlert = lowBatteryAlert;
    }
}
