package com.ebike.dto;

import com.ebike.model.TripStatus;
import java.time.LocalDateTime;

public class TripDto {

    private Long tripId;
    private Long riderId;
    private String riderName;
    private Long bikeId;
    private String bikeCode;
    private Long startStationId;
    private String startStationName;
    private Long endStationId;
    private String endStationName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMinutes;
    private Double distanceKm;
    private Double fare;
    private TripStatus status;

    public TripDto() {}

    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    public Long getRiderId() {
        return riderId;
    }
    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }
    public String getRiderName() {
        return riderName;
    }
    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }
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
    public Long getStartStationId() {
        return startStationId;
    }
    public void setStartStationId(Long startStationId) {
        this.startStationId = startStationId;
    }
    public String getStartStationName() {
        return startStationName;
    }
    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }
    public Long getEndStationId() {
        return endStationId;
    }
    public void setEndStationId(Long endStationId) {
        this.endStationId = endStationId;
    }
    public String getEndStationName() {
        return endStationName;
    }
    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Long getDurationMinutes() {
        return durationMinutes;
    }
    public void setDurationMinutes(Long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    public Double getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
    public Double getFare() {
        return fare;
    }
    public void setFare(Double fare) {
        this.fare = fare;
    }
    public TripStatus getStatus() {
        return status;
    }
    public void setStatus(TripStatus status) {
        this.status = status;
    }
}
