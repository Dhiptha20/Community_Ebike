package com.ebike.dto;

import jakarta.validation.constraints.NotNull;

public class TripStartDto {

    private Long riderId;

    @NotNull(message = "Bike ID is required")
    private Long bikeId;

    private Long startStationId;

    public TripStartDto() {}

    public Long getRiderId() {
        return riderId;
    }
    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }
    public Long getBikeId() {
        return bikeId;
    }
    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }
    public Long getStartStationId() {
        return startStationId;
    }
    public void setStartStationId(Long startStationId) {
        this.startStationId = startStationId;
    }
}
