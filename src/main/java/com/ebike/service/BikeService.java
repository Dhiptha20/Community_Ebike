package com.ebike.service;

import com.ebike.dto.BikeDto;
import com.ebike.exception.BadRequestException;
import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.Bike;
import com.ebike.model.BikeStatus;
import com.ebike.model.DockingStation;
import com.ebike.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BikeService {

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private StationService stationService;

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public List<Bike> getAvailableBikes() {
        return bikeRepository.findByStatus(BikeStatus.AVAILABLE);
    }

    public List<Bike> getLowBatteryBikes() {
        return bikeRepository.findByBatteryLevelLessThan(20);
    }

    public Bike getBikeById(Long id) {
        return bikeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found with ID: " + id));
    }

    public Bike getBikeByCode(String code) {
        return bikeRepository.findByBikeCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found with code: " + code));
    }

    public List<Bike> searchBikes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBikes();
        }
        return bikeRepository.findByBikeCodeContainingIgnoreCase(keyword.trim());
    }

    @Transactional
    public Bike createBike(BikeDto dto) {
        if (bikeRepository.existsByBikeCode(dto.getBikeCode().trim())) {
            throw new BadRequestException("Bike with code '" + dto.getBikeCode() + "' already exists");
        }

        Bike bike = new Bike();
        bike.setBikeCode(dto.getBikeCode().trim().toUpperCase());
        bike.setBatteryLevel(dto.getBatteryLevel() != null ? dto.getBatteryLevel() : 100);
        bike.setStatus(dto.getStatus() != null ? dto.getStatus() : BikeStatus.AVAILABLE);
        bike.setGpsLatitude(dto.getGpsLatitude() != null ? dto.getGpsLatitude() : 12.9716);
        bike.setGpsLongitude(dto.getGpsLongitude() != null ? dto.getGpsLongitude() : 77.5946);

        if (dto.getStationId() != null) {
            DockingStation station = stationService.getStationById(dto.getStationId());
            bike.setDockingStation(station);
        }

        return bikeRepository.save(bike);
    }

    @Transactional
    public Bike updateBike(Long id, BikeDto dto) {
        Bike bike = getBikeById(id);

        if (!bike.getBikeCode().equalsIgnoreCase(dto.getBikeCode().trim())) {
            if (bikeRepository.existsByBikeCode(dto.getBikeCode().trim())) {
                throw new BadRequestException("Bike code '" + dto.getBikeCode() + "' is already in use");
            }
            bike.setBikeCode(dto.getBikeCode().trim().toUpperCase());
        }

        if (dto.getBatteryLevel() != null) {
            bike.setBatteryLevel(dto.getBatteryLevel());
        }
        if (dto.getStatus() != null) {
            bike.setStatus(dto.getStatus());
        }
        if (dto.getGpsLatitude() != null) {
            bike.setGpsLatitude(dto.getGpsLatitude());
        }
        if (dto.getGpsLongitude() != null) {
            bike.setGpsLongitude(dto.getGpsLongitude());
        }
        if (dto.getStationId() != null) {
            DockingStation station = stationService.getStationById(dto.getStationId());
            bike.setDockingStation(station);
        } else {
            bike.setDockingStation(null);
        }

        return bikeRepository.save(bike);
    }

    @Transactional
    public void deleteBike(Long id) {
        Bike bike = getBikeById(id);
        bikeRepository.delete(bike);
    }

    @Transactional
    public Bike updateStatus(Long id, BikeStatus status) {
        Bike bike = getBikeById(id);
        bike.setStatus(status);
        return bikeRepository.save(bike);
    }
}
