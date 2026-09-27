package com.ebike.service;

import com.ebike.model.*;
import com.ebike.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private DockingStationRepository stationRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public Map<String, Object> getAdminDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        long totalBikes = bikeRepository.count();
        long availableBikes = bikeRepository.countByStatus(BikeStatus.AVAILABLE);
        long inUseBikes = bikeRepository.countByStatus(BikeStatus.IN_USE);
        long maintenanceBikes = bikeRepository.countByStatus(BikeStatus.MAINTENANCE);
        long chargingBikes = bikeRepository.countByStatus(BikeStatus.CHARGING);
        long disabledBikes = bikeRepository.countByStatus(BikeStatus.DISABLED);
        long lowBatteryBikes = bikeRepository.countByBatteryLevelLessThan(20);

        long totalStations = stationRepository.count();
        long activeRiders = riderRepository.count();
        long ongoingTrips = tripRepository.countByStatus(TripStatus.ONGOING);
        long completedTrips = tripRepository.countByStatus(TripStatus.COMPLETED);

        Double totalRevenue = paymentRepository.getTotalSuccessfulRevenue();
        long pendingInvoices = invoiceRepository.countByStatus(InvoiceStatus.UNPAID);

        Double totalAllocated = budgetRepository.getTotalAllocatedBudget();
        Double totalSpend = budgetRepository.getTotalActualSpend();
        double budgetUtilization = (totalAllocated != null && totalAllocated > 0)
                ? Math.round(((totalSpend != null ? totalSpend : 0.0) / totalAllocated * 100.0) * 10.0) / 10.0
                : 0.0;

        metrics.put("totalBikes", totalBikes);
        metrics.put("availableBikes", availableBikes);
        metrics.put("inUseBikes", inUseBikes);
        metrics.put("maintenanceBikes", maintenanceBikes);
        metrics.put("chargingBikes", chargingBikes);
        metrics.put("disabledBikes", disabledBikes);
        metrics.put("lowBatteryBikes", lowBatteryBikes);

        metrics.put("totalStations", totalStations);
        metrics.put("activeRiders", activeRiders);
        metrics.put("ongoingTrips", ongoingTrips);
        metrics.put("completedTrips", completedTrips);
        metrics.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        metrics.put("pendingInvoices", pendingInvoices);
        metrics.put("budgetUtilization", budgetUtilization);
        metrics.put("totalAllocated", totalAllocated != null ? totalAllocated : 0.0);
        metrics.put("totalSpend", totalSpend != null ? totalSpend : 0.0);

        return metrics;
    }

    public Map<String, Object> getInvoicingDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        long totalInvoices = invoiceRepository.count();
        long pendingInvoices = invoiceRepository.countByStatus(InvoiceStatus.UNPAID);
        long paidInvoices = invoiceRepository.countByStatus(InvoiceStatus.PAID);
        long totalPOs = purchaseOrderRepository.count();

        Double totalRevenue = paymentRepository.getTotalSuccessfulRevenue();
        Double totalSpend = budgetRepository.getTotalActualSpend();
        Double totalAllocated = budgetRepository.getTotalAllocatedBudget();

        metrics.put("totalInvoices", totalInvoices);
        metrics.put("pendingInvoices", pendingInvoices);
        metrics.put("paidInvoices", paidInvoices);
        metrics.put("totalPOs", totalPOs);
        metrics.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        metrics.put("totalSpend", totalSpend != null ? totalSpend : 0.0);
        metrics.put("totalAllocated", totalAllocated != null ? totalAllocated : 0.0);

        return metrics;
    }

    public Map<String, Object> getRiderDashboardMetrics(Rider rider) {
        Map<String, Object> metrics = new HashMap<>();

        List<Trip> riderTrips = tripRepository.findByRiderOrderByStartTimeDesc(rider);
        long completedTrips = riderTrips.stream().filter(t -> t.getStatus() == TripStatus.COMPLETED).count();

        List<Invoice> invoices = invoiceRepository.findByRiderOrderByCreatedAtDesc(rider);
        long unpaidInvoices = invoices.stream().filter(i -> i.getStatus() == InvoiceStatus.UNPAID).count();

        Double totalSpent = paymentRepository.getTotalSpentByRider(rider);

        metrics.put("completedTrips", completedTrips);
        metrics.put("unpaidInvoices", unpaidInvoices);
        metrics.put("totalSpent", totalSpent != null ? totalSpent : 0.0);

        return metrics;
    }
}
