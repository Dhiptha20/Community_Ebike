package com.ebike.repository;

import com.ebike.model.Bike;
import com.ebike.model.Rider;
import com.ebike.model.Trip;
import com.ebike.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRiderOrderByStartTimeDesc(Rider rider);
    List<Trip> findByBikeOrderByStartTimeDesc(Bike bike);
    List<Trip> findByStatus(TripStatus status);
    Optional<Trip> findByRiderAndStatus(Rider rider, TripStatus status);
    Optional<Trip> findByBikeAndStatus(Bike bike, TripStatus status);
    long countByStatus(TripStatus status);
    List<Trip> findAllByOrderByStartTimeDesc();
}
