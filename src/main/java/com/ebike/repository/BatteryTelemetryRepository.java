package com.ebike.repository;

import com.ebike.model.BatteryTelemetry;
import com.ebike.model.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryTelemetryRepository extends JpaRepository<BatteryTelemetry, Long> {
    List<BatteryTelemetry> findByBikeOrderByReadingTimeDesc(Bike bike);
    List<BatteryTelemetry> findByLowBatteryAlertTrueOrderByReadingTimeDesc();
    List<BatteryTelemetry> findTop50ByOrderByReadingTimeDesc();
}
