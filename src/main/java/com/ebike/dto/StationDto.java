package com.ebike.dto;

import com.ebike.model.StationStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StationDto {

    private Long stationId;

    @NotBlank(message = "Station name is required")
    private String stationName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Total slots is required")
    @Min(1)
    private Integer totalSlots;

    private Integer availableSlots;

    private StationStatus status = StationStatus.ACTIVE;

    public StationDto() {}

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
}
