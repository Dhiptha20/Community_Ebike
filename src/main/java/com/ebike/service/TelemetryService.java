package com.ebike.service;

import com.ebike.dto.TelemetryDto;
import com.ebike.model.BatteryTelemetry;
import com.ebike.model.Bike;
import com.ebike.repository.BatteryTelemetryRepository;
import com.ebike.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetryService {

    @Autowired
    private BatteryTelemetryRepository telemetryRepository;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BikeRepository bikeRepository;

    public List<BatteryTelemetry> getRecentTelemetry() {
        return telemetryRepository.findTop50ByOrderByReadingTimeDesc();
    }

    public List<BatteryTelemetry> getTelemetryByBike(Long bikeId) {
        Bike bike = bikeService.getBikeById(bikeId);
        return telemetryRepository.findByBikeOrderByReadingTimeDesc(bike);
    }

    public List<BatteryTelemetry> getLowBatteryAlerts() {
        return telemetryRepository.findByLowBatteryAlertTrueOrderByReadingTimeDesc();
    }

    @Transactional
    public BatteryTelemetry recordTelemetry(TelemetryDto dto) {
        Bike bike = bikeService.getBikeById(dto.getBikeId());

        // Update bike's current status and GPS
        bike.setBatteryLevel(dto.getBatteryLevel());
        bike.setGpsLatitude(dto.getLatitude());
        bike.setGpsLongitude(dto.getLongitude());
        bikeRepository.save(bike);

        // Record telemetry log
        BatteryTelemetry telemetry = new BatteryTelemetry();
        telemetry.setBike(bike);
        telemetry.setBatteryLevel(dto.getBatteryLevel());
        telemetry.setLatitude(dto.getLatitude());
        telemetry.setLongitude(dto.getLongitude());
        telemetry.setReadingTime(LocalDateTime.now());
        telemetry.setLowBatteryAlert(dto.getBatteryLevel() < 20);

        return telemetryRepository.save(telemetry);
    }
}
