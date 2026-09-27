package com.ebike.repository;

import com.ebike.model.Bike;
import com.ebike.model.BikeStatus;
import com.ebike.model.DockingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    Optional<Bike> findByBikeCode(String bikeCode);
    boolean existsByBikeCode(String bikeCode);
    List<Bike> findByStatus(BikeStatus status);
    List<Bike> findByDockingStation(DockingStation dockingStation);
    List<Bike> findByBatteryLevelLessThan(Integer level);
    List<Bike> findByBikeCodeContainingIgnoreCase(String keyword);
    long countByStatus(BikeStatus status);
    long countByBatteryLevelLessThan(Integer level);
}
