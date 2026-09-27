package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rider_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rider rider;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bike_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Bike bike;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "start_station_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DockingStation startStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_station_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DockingStation endStation;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = true)
    private LocalDateTime endTime;

    @Column(nullable = true)
    private Long durationMinutes = 0L;

    @Column(nullable = true)
    private Double distanceKm = 0.0;

    @Column(nullable = true)
    private Double fare = 0.0;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripStatus status = TripStatus.ONGOING;

    public Trip() {}

    public Trip(Rider rider, Bike bike, DockingStation startStation, LocalDateTime startTime) {
        this.rider = rider;
        this.bike = bike;
        this.startStation = startStation;
        this.startTime = startTime != null ? startTime : LocalDateTime.now();
        this.status = TripStatus.ONGOING;
    }

    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    public Rider getRider() {
        return rider;
    }
    public void setRider(Rider rider) {
        this.rider = rider;
    }
    public Bike getBike() {
        return bike;
    }
    public void setBike(Bike bike) {
        this.bike = bike;
    }
    public DockingStation getStartStation() {
        return startStation;
    }
    public void setStartStation(DockingStation startStation) {
        this.startStation = startStation;
    }
    public DockingStation getEndStation() {
        return endStation;
    }
    public void setEndStation(DockingStation endStation) {
        this.endStation = endStation;
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
