package com.ebike.service;

import com.ebike.dto.TripEndDto;
import com.ebike.dto.TripStartDto;
import com.ebike.exception.BadRequestException;
import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.*;
import com.ebike.repository.BikeRepository;
import com.ebike.repository.RiderRepository;
import com.ebike.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private TariffService tariffService;

    @Autowired
    private InvoiceService invoiceService;

    public List<Trip> getAllTrips() {
        return tripRepository.findAllByOrderByStartTimeDesc();
    }

    public List<Trip> getTripsByRider(Rider rider) {
        return tripRepository.findByRiderOrderByStartTimeDesc(rider);
    }

    public Optional<Trip> getActiveTripByRider(Rider rider) {
        return tripRepository.findByRiderAndStatus(rider, TripStatus.ONGOING);
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + id));
    }

    @Transactional
    public Trip unlockBikeAndStartTrip(TripStartDto dto) {
        // 1. Verify rider
        Rider rider = riderRepository.findById(dto.getRiderId())
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found with ID: " + dto.getRiderId()));

        // Check if rider already has an ongoing trip
        Optional<Trip> existingOngoing = tripRepository.findByRiderAndStatus(rider, TripStatus.ONGOING);
        if (existingOngoing.isPresent()) {
            throw new BadRequestException("You already have an ongoing trip (Trip #" + existingOngoing.get().getTripId() + "). Please complete it first.");
        }

        // 2. Verify bike exists
        Bike bike = bikeRepository.findById(dto.getBikeId())
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found with ID: " + dto.getBikeId()));

        // 3. Verify bike status is AVAILABLE
        if (bike.getStatus() != BikeStatus.AVAILABLE) {
            throw new BadRequestException("Bike " + bike.getBikeCode() + " is currently " + bike.getStatus() + " and cannot be unlocked.");
        }

        // 4. Change bike status to IN_USE
        bike.setStatus(BikeStatus.IN_USE);

        // 5. Update station slots: the bike was in a station, so leaving frees up 1 slot
        DockingStation startStation = bike.getDockingStation();
        if (startStation != null) {
            stationService.incrementAvailableSlots(startStation);
            bike.setDockingStation(null); // bike is now on the road
        }
        bikeRepository.save(bike);

        // 6. Create Trip
        Trip trip = new Trip();
        trip.setRider(rider);
        trip.setBike(bike);
        trip.setStartStation(startStation);
        trip.setStartTime(LocalDateTime.now());
        trip.setStatus(TripStatus.ONGOING);
        trip.setDistanceKm(0.0);
        trip.setFare(0.0);
        trip.setDurationMinutes(0L);

        return tripRepository.save(trip);
    }

    @Transactional
    public Trip completeTrip(TripEndDto dto) {
        Trip trip = getTripById(dto.getTripId());

        if (trip.getStatus() != TripStatus.ONGOING) {
            throw new BadRequestException("Trip #" + trip.getTripId() + " is already " + trip.getStatus());
        }

        // 1. Record end time
        LocalDateTime endTime = LocalDateTime.now();
        trip.setEndTime(endTime);

        // 2. Calculate duration
        long durationMinutes = Duration.between(trip.getStartTime(), endTime).toMinutes();
        if (durationMinutes < 1) {
            durationMinutes = 1; // Minimum 1 minute billing
        }
        trip.setDurationMinutes(durationMinutes);

        // 3. Distance
        double distance = dto.getDistanceKm() != null ? dto.getDistanceKm() : 1.5;
        trip.setDistanceKm(distance);

        // 4. Calculate fare dynamically from active tariff
        Double fare = tariffService.calculateFare(durationMinutes, distance);
        trip.setFare(fare);

        // 5. Change trip status to COMPLETED
        trip.setStatus(TripStatus.COMPLETED);

        // 6. Dock bike and update station
        Bike bike = trip.getBike();
        bike.setStatus(BikeStatus.AVAILABLE);

        if (dto.getEndStationId() != null) {
            DockingStation endStation = stationService.getStationById(dto.getEndStationId());
            stationService.decrementAvailableSlots(endStation);
            bike.setDockingStation(endStation);
            trip.setEndStation(endStation);
        }
        bikeRepository.save(bike);
        Trip savedTrip = tripRepository.save(trip);

        // 7. Automatically generate Invoice
        invoiceService.createTripInvoice(savedTrip);

        return savedTrip;
    }
}
