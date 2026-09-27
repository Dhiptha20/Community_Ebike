package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.PaymentDto;
import com.ebike.model.Payment;
import com.ebike.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentApiController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Payment>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponseDto.success("Payments retrieved successfully", payments));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Payment>> createPayment(@Valid @RequestBody PaymentDto dto) {
        Payment payment = paymentService.processPayment(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Payment processed successfully", payment), HttpStatus.CREATED);
    }
}
