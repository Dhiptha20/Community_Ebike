package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.TelemetryDto;
import com.ebike.model.BatteryTelemetry;
import com.ebike.model.Bike;
import com.ebike.service.BikeService;
import com.ebike.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryApiController {

    @Autowired
    private TelemetryService telemetryService;

    @Autowired
    private BikeService bikeService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<BatteryTelemetry>> recordTelemetry(@Valid @RequestBody TelemetryDto dto) {
        BatteryTelemetry recorded = telemetryService.recordTelemetry(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Telemetry recorded successfully", recorded), HttpStatus.CREATED);
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponseDto<List<Bike>>> getLowBatteryAlerts() {
        List<Bike> lowBatteryBikes = bikeService.getLowBatteryBikes();
        return ResponseEntity.ok(ApiResponseDto.success("Low battery alert bikes retrieved", lowBatteryBikes));
    }

    @GetMapping("/live")
    public ResponseEntity<ApiResponseDto<List<Bike>>> getLiveFleetTelemetry() {
        List<Bike> bikes = bikeService.getAllBikes();
        return ResponseEntity.ok(ApiResponseDto.success("Live fleet telemetry retrieved", bikes));
    }

    @GetMapping("/bike/{bikeId}")
    public ResponseEntity<ApiResponseDto<List<BatteryTelemetry>>> getTelemetryByBike(@PathVariable Long bikeId) {
        List<BatteryTelemetry> telemetryList = telemetryService.getTelemetryByBike(bikeId);
        return ResponseEntity.ok(ApiResponseDto.success("Telemetry records found", telemetryList));
    }
}
