package com.ebike.repository;

import com.ebike.model.Invoice;
import com.ebike.model.Payment;
import com.ebike.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByRiderOrderByPaymentDateDesc(Rider rider);
    List<Payment> findByInvoice(Invoice invoice);
    List<Payment> findAllByOrderByPaymentDateDesc();

    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p WHERE p.status = com.ebike.model.PaymentStatus.SUCCESS")
    Double getTotalSuccessfulRevenue();

    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p WHERE p.rider = :rider AND p.status = com.ebike.model.PaymentStatus.SUCCESS")
    Double getTotalSpentByRider(Rider rider);
}
