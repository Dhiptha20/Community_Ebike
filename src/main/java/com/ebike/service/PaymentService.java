package com.ebike.service;

import com.ebike.dto.PaymentDto;
import com.ebike.exception.BadRequestException;
import com.ebike.model.*;
import com.ebike.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AccountingService accountingService;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public List<Payment> getPaymentsByRider(Rider rider) {
        return paymentRepository.findByRiderOrderByPaymentDateDesc(rider);
    }

    public Double getTotalRevenue() {
        return paymentRepository.getTotalSuccessfulRevenue();
    }

    public Double getTotalSpentByRider(Rider rider) {
        return paymentRepository.getTotalSpentByRider(rider);
    }

    @Transactional
    public Payment processPayment(PaymentDto dto) {
        Invoice invoice = invoiceService.getInvoiceById(dto.getInvoiceId());

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice #" + invoice.getInvoiceId() + " is already paid.");
        }

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setRider(invoice.getRider());
        payment.setAmount(dto.getAmount() != null ? dto.getAmount() : invoice.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionRef("TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000));

        Payment savedPayment = paymentRepository.save(payment);

        // Update Invoice status to PAID
        invoiceService.updateStatus(invoice.getInvoiceId(), InvoiceStatus.PAID);

        // Record balanced accounting journal entry:
        // Debit: Cash/Bank (1010)
        // Credit: Rider Rental Revenue (4010)
        accountingService.recordTransaction(
                "1010",
                "4010",
                savedPayment.getAmount(),
                "Rental fare payment for Invoice #" + invoice.getInvoiceId() + " via " + savedPayment.getPaymentMethod(),
                savedPayment.getTransactionRef()
        );

        return savedPayment;
    }
}
