package com.ebike.dto;

import com.ebike.model.BikeStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BikeDto {

    private Long bikeId;

    @NotBlank(message = "Bike code is required")
    private String bikeCode;

    private Long stationId;

    @NotNull(message = "Battery level is required")
    @Min(0)
    @Max(100)
    private Integer batteryLevel;

    private BikeStatus status = BikeStatus.AVAILABLE;

    private Double gpsLatitude = 12.9716;

    private Double gpsLongitude = 77.5946;

    public BikeDto() {}

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
    public Long getStationId() {
        return stationId;
    }
    public void setStationId(Long stationId) {
        this.stationId = stationId;
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
}
