package com.ebike.controller;

import com.ebike.model.Budget;
import com.ebike.model.Trip;
import com.ebike.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private TripService tripService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public String viewReports(Model model) {
        Map<String, Object> adminMetrics = dashboardService.getAdminDashboardMetrics();
        model.addAllAttributes(adminMetrics);

        List<Budget> budgets = budgetService.getAllBudgets();
        List<Trip> trips = tripService.getAllTrips();

        double totalDistance = trips.stream()
                .filter(t -> t.getDistanceKm() != null)
                .mapToDouble(Trip::getDistanceKm)
                .sum();
        long totalRideMinutes = trips.stream()
                .filter(t -> t.getDurationMinutes() != null)
                .mapToLong(Trip::getDurationMinutes)
                .sum();

        model.addAttribute("budgets", budgets);
        model.addAttribute("trips", trips);
        model.addAttribute("totalDistanceKm", Math.round(totalDistance * 10.0) / 10.0);
        model.addAttribute("totalRideMinutes", totalRideMinutes);
        model.addAttribute("lowBatteryCount", bikeService.getLowBatteryBikes().size());

        return "reports";
    }
}
