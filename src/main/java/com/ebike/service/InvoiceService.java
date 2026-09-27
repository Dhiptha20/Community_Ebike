package com.ebike.service;

import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.Invoice;
import com.ebike.model.InvoiceStatus;
import com.ebike.model.Rider;
import com.ebike.model.Trip;
import com.ebike.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Invoice> getInvoicesByRider(Rider rider) {
        return invoiceRepository.findByRiderOrderByCreatedAtDesc(rider);
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
    }

    public List<Invoice> getPendingInvoices() {
        return invoiceRepository.findByStatus(InvoiceStatus.UNPAID);
    }

    @Transactional
    public Invoice createTripInvoice(Trip trip) {
        Invoice invoice = new Invoice();
        invoice.setRider(trip.getRider());
        invoice.setTrip(trip);
        invoice.setAmount(trip.getFare() != null ? trip.getFare() : 0.0);
        invoice.setDueDate(LocalDate.now().plusDays(7));
        invoice.setStatus(InvoiceStatus.UNPAID);
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice updateStatus(Long invoiceId, InvoiceStatus status) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
}
