package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.BikeDto;
import com.ebike.model.Bike;
import com.ebike.service.BikeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bikes")
public class BikeApiController {

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Bike>>> getAllBikes() {
        List<Bike> bikes = bikeService.getAllBikes();
        return ResponseEntity.ok(ApiResponseDto.success("Bikes retrieved successfully", bikes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Bike>> getBikeById(@PathVariable Long id) {
        Bike bike = bikeService.getBikeById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Bike found", bike));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Bike>> createBike(@Valid @RequestBody BikeDto dto) {
        Bike created = bikeService.createBike(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Bike created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Bike>> updateBike(@PathVariable Long id, @Valid @RequestBody BikeDto dto) {
        Bike updated = bikeService.updateBike(id, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Bike updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);
        return ResponseEntity.ok(ApiResponseDto.success("Bike deleted successfully", null));
    }
}
