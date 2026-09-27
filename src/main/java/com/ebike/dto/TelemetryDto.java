package com.ebike.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TelemetryDto {

    @NotNull(message = "Bike ID is required")
    private Long bikeId;

    @NotNull(message = "Battery level is required")
    @Min(0)
    @Max(100)
    private Integer batteryLevel;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    public TelemetryDto() {}

    public TelemetryDto(Long bikeId, Integer batteryLevel, Double latitude, Double longitude) {
        this.bikeId = bikeId;
        this.batteryLevel = batteryLevel;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public Long getBikeId() {
        return bikeId;
    }
    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }
    public Integer getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
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
}
