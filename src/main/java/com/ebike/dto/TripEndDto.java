package com.ebike.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class TripEndDto {

    private Long tripId;

    private Long endStationId;

    @NotNull(message = "Distance in km is required")
    @DecimalMin(value = "0.0", message = "Distance cannot be negative")
    private Double distanceKm;

    public TripEndDto() {}

    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    public Long getEndStationId() {
        return endStationId;
    }
    public void setEndStationId(Long endStationId) {
        this.endStationId = endStationId;
    }
    public Double getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
}
