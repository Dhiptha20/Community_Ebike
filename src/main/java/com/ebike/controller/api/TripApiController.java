package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.TripEndDto;
import com.ebike.dto.TripStartDto;
import com.ebike.model.Trip;
import com.ebike.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripApiController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Trip>>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return ResponseEntity.ok(ApiResponseDto.success("Trips retrieved successfully", trips));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Trip>> startTrip(@Valid @RequestBody TripStartDto dto) {
        Trip trip = tripService.unlockBikeAndStartTrip(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Bike unlocked and trip started successfully", trip), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponseDto<Trip>> completeTrip(@PathVariable Long id, @RequestBody TripEndDto dto) {
        dto.setTripId(id);
        Trip completedTrip = tripService.completeTrip(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Trip completed and invoice generated successfully", completedTrip));
    }
}
