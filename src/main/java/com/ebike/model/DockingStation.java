package com.ebike.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "docking_stations")
public class DockingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    @NotBlank(message = "Station name is required")
    @Column(nullable = false, length = 100)
    private String stationName;

    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 255)
    private String location;

    @NotNull(message = "Total slots is required")
    @Min(value = 1, message = "Station must have at least 1 slot")
    @Column(nullable = false)
    private Integer totalSlots;

    @NotNull(message = "Available slots is required")
    @Min(value = 0, message = "Available slots cannot be negative")
    @Column(nullable = false)
    private Integer availableSlots;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StationStatus status = StationStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.availableSlots == null) {
            this.availableSlots = this.totalSlots;
        }
    }

    public DockingStation() {}

    public DockingStation(String stationName, String location, Integer totalSlots, Integer availableSlots, StationStatus status) {
        this.stationName = stationName;
        this.location = location;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots != null ? availableSlots : totalSlots;
        this.status = status != null ? status : StationStatus.ACTIVE;
    }

    public int getOccupiedSlots() {
        return (totalSlots != null && availableSlots != null) ? (totalSlots - availableSlots) : 0;
    }

    // Getters and Setters
    public Long getStationId() {
        return stationId;
    }
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Integer getTotalSlots() {
        return totalSlots;
    }
    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }
    public Integer getAvailableSlots() {
        return availableSlots;
    }
    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }
    public StationStatus getStatus() {
        return status;
    }
    public void setStatus(StationStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
