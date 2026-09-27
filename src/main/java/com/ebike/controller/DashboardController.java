package com.ebike.controller;

import com.ebike.model.*;
import com.ebike.security.CustomUserDetails;
import com.ebike.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private StationService stationService;

    @Autowired
    private TripService tripService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        if (userDetails.getUser().getRole() == Role.ROLE_RIDER) {
            return "redirect:/rider/dashboard";
        } else if (userDetails.getUser().getRole() == Role.ROLE_INVOICING_USER) {
            return "redirect:/invoicing/dashboard";
        }

        // ADMIN Dashboard
        Map<String, Object> metrics = dashboardService.getAdminDashboardMetrics();
        model.addAllAttributes(metrics);

        List<Bike> lowBatteryBikes = bikeService.getLowBatteryBikes();
        List<Bike> allBikes = bikeService.getAllBikes();
        List<DockingStation> stations = stationService.getAllStations();
        List<Trip> recentTrips = tripService.getAllTrips();
        List<Budget> budgets = budgetService.getAllBudgets();

        model.addAttribute("lowBatteryList", lowBatteryBikes);
        model.addAttribute("allBikes", allBikes);
        model.addAttribute("stations", stations);
        model.addAttribute("recentTrips", recentTrips.size() > 5 ? recentTrips.subList(0, 5) : recentTrips);
        model.addAttribute("budgets", budgets);

        return "dashboard";
    }

    @GetMapping("/rider/dashboard")
    public String riderDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Rider rider = userService.getRiderByUsername(userDetails.getUsername());
        Map<String, Object> metrics = dashboardService.getRiderDashboardMetrics(rider);
        model.addAllAttributes(metrics);

        Optional<Trip> activeTrip = tripService.getActiveTripByRider(rider);
        model.addAttribute("activeTrip", activeTrip.orElse(null));

        List<Bike> availableBikes = bikeService.getAvailableBikes();
        model.addAttribute("availableBikes", availableBikes);

        List<DockingStation> stations = stationService.getActiveStations();
        model.addAttribute("stations", stations);

        List<Invoice> invoices = invoiceService.getInvoicesByRider(rider);
        model.addAttribute("invoices", invoices);

        List<Trip> trips = tripService.getTripsByRider(rider);
        model.addAttribute("trips", trips);
        model.addAttribute("rider", rider);

        return "rider/dashboard";
    }

    @GetMapping("/invoicing/dashboard")
    public String invoicingDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Map<String, Object> metrics = dashboardService.getInvoicingDashboardMetrics();
        model.addAllAttributes(metrics);

        List<Invoice> recentInvoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", recentInvoices.size() > 10 ? recentInvoices.subList(0, 10) : recentInvoices);

        List<PurchaseOrder> pos = purchaseOrderService.getAllPurchaseOrders();
        model.addAttribute("purchaseOrders", pos.size() > 10 ? pos.subList(0, 10) : pos);

        List<Budget> budgets = budgetService.getAllBudgets();
        model.addAttribute("budgets", budgets);

        return "invoicing/dashboard";
    }
}
