package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.PurchaseOrderDto;
import com.ebike.model.PurchaseOrder;
import com.ebike.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderApiController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<PurchaseOrder>>> getAllPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(ApiResponseDto.success("Purchase orders retrieved successfully", orders));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<PurchaseOrder>> createPurchaseOrder(@Valid @RequestBody PurchaseOrderDto dto) {
        PurchaseOrder created = purchaseOrderService.createPurchaseOrder(dto);
        return new ResponseEntity<>(ApiResponseDto.success("Purchase order created successfully", created), HttpStatus.CREATED);
    }
}
