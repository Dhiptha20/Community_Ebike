package com.ebike.controller.api;

import com.ebike.dto.ApiResponseDto;
import com.ebike.dto.InvoiceDto;
import com.ebike.model.Invoice;
import com.ebike.model.InvoiceStatus;
import com.ebike.model.Rider;
import com.ebike.repository.RiderRepository;
import com.ebike.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceApiController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RiderRepository riderRepository;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Invoice>>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(ApiResponseDto.success("Invoices retrieved successfully", invoices));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Invoice>> createInvoice(@Valid @RequestBody InvoiceDto dto) {
        Rider rider = riderRepository.findById(dto.getRiderId())
                .orElseThrow(() -> new IllegalArgumentException("Rider not found with ID: " + dto.getRiderId()));

        Invoice invoice = new Invoice();
        invoice.setRider(rider);
        invoice.setAmount(dto.getAmount());
        invoice.setDueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(7));
        invoice.setStatus(dto.getStatus() != null ? dto.getStatus() : InvoiceStatus.UNPAID);

        return new ResponseEntity<>(ApiResponseDto.success("Invoice created successfully", invoice), HttpStatus.CREATED);
    }
}
