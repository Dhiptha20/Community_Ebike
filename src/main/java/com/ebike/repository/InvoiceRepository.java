package com.ebike.repository;

import com.ebike.model.Invoice;
import com.ebike.model.InvoiceStatus;
import com.ebike.model.Rider;
import com.ebike.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByRiderOrderByCreatedAtDesc(Rider rider);
    List<Invoice> findByStatus(InvoiceStatus status);
    Optional<Invoice> findByTrip(Trip trip);
    long countByStatus(InvoiceStatus status);
    List<Invoice> findAllByOrderByCreatedAtDesc();
}
