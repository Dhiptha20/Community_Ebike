package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.model.RentalTariff;
import com.ebike.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffApiController {

    @Autowired
    private TariffService tariffService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<RentalTariff>>> getAllTariffs() {
        List<RentalTariff> tariffs = tariffService.getAllTariffs();
        return ResponseEntity.ok(ApiResponseDto.success("Tariffs retrieved successfully", tariffs));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDto<RentalTariff>> getActiveTariff() {
        RentalTariff tariff = tariffService.getActiveTariff();
        return ResponseEntity.ok(ApiResponseDto.success("Active tariff retrieved successfully", tariff));
    }
}
