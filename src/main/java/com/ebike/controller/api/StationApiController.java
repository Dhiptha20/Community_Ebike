package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.StationDto;
import com.ebike.model.DockingStation;
import com.ebike.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationApiController {

    @Autowired
    private StationService stationService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<DockingStation>>> getAllStations() {
        List<DockingStation> stations = stationService.getAllStations();
        return ResponseEntity.ok(ApiResponseDto.success("Stations retrieved successfully", stations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DockingStation>> getStationById(@PathVariable Long id) {
        DockingStation station = stationService.getStationById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Station found", station));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<DockingStation>> createStation(@Valid @RequestBody StationDto dto) {
        DockingStation created = stationService.createStation(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Station created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DockingStation>> updateStation(@PathVariable Long id, @Valid @RequestBody StationDto dto) {
        DockingStation updated = stationService.updateStation(id, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Station updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.ok(ApiResponseDto.success("Station deleted successfully", null));
    }
}
