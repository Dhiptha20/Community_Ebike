package com.ebike.service;

import com.ebike.dto.StationDto;
import com.ebike.exception.BadRequestException;
import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.DockingStation;
import com.ebike.model.StationStatus;
import com.ebike.repository.DockingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StationService {

    @Autowired
    private DockingStationRepository stationRepository;

    public List<DockingStation> getAllStations() {
        return stationRepository.findAll();
    }

    public List<DockingStation> getActiveStations() {
        return stationRepository.findByStatus(StationStatus.ACTIVE);
    }

    public DockingStation getStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docking station not found with ID: " + id));
    }

    @Transactional
    public DockingStation createStation(StationDto dto) {
        DockingStation station = new DockingStation();
        station.setStationName(dto.getStationName().trim());
        station.setLocation(dto.getLocation().trim());
        station.setTotalSlots(dto.getTotalSlots());
        station.setAvailableSlots(dto.getAvailableSlots() != null ? dto.getAvailableSlots() : dto.getTotalSlots());
        station.setStatus(dto.getStatus() != null ? dto.getStatus() : StationStatus.ACTIVE);
        return stationRepository.save(station);
    }

    @Transactional
    public DockingStation updateStation(Long id, StationDto dto) {
        DockingStation station = getStationById(id);
        station.setStationName(dto.getStationName().trim());
        station.setLocation(dto.getLocation().trim());
        station.setTotalSlots(dto.getTotalSlots());
        if (dto.getAvailableSlots() != null) {
            station.setAvailableSlots(Math.min(dto.getAvailableSlots(), dto.getTotalSlots()));
        }
        if (dto.getStatus() != null) {
            station.setStatus(dto.getStatus());
        }
        return stationRepository.save(station);
    }

    @Transactional
    public void deleteStation(Long id) {
        DockingStation station = getStationById(id);
        stationRepository.delete(station);
    }

    @Transactional
    public void incrementAvailableSlots(DockingStation station) {
        if (station != null) {
            int current = station.getAvailableSlots();
            if (current < station.getTotalSlots()) {
                station.setAvailableSlots(current + 1);
                stationRepository.save(station);
            }
        }
    }

    @Transactional
    public void decrementAvailableSlots(DockingStation station) {
        if (station != null) {
            int current = station.getAvailableSlots();
            if (current > 0) {
                station.setAvailableSlots(current - 1);
                stationRepository.save(station);
            } else {
                throw new BadRequestException("No available docking slots at station: " + station.getStationName());
            }
        }
    }
}
