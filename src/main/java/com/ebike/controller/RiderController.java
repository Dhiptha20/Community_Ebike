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
import java.util.Optional;

@Controller
public class RiderController {

    @Autowired
    private UserService userService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private TripService tripService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private StationService stationService;

    // Admin view of all riders
    @GetMapping("/riders")
    public String listRiders(Model model) {
        List<Rider> riders = userService.findAllRiders();
        model.addAttribute("riders", riders);
        return "riders";
    }

    // Rider views available bikes
    @GetMapping("/rider/bikes")
    public String riderBikes(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Rider rider = userService.getRiderByUsername(userDetails.getUsername());
        List<Bike> availableBikes = bikeService.getAvailableBikes();
        List<DockingStation> stations = stationService.getActiveStations();
        Optional<Trip> activeTrip = tripService.getActiveTripByRider(rider);

        model.addAttribute("rider", rider);
        model.addAttribute("availableBikes", availableBikes);
        model.addAttribute("stations", stations);
        model.addAttribute("activeTrip", activeTrip.orElse(null));

        return "rider/bikes";
    }

    // Rider views trip history
    @GetMapping("/rider/trips")
    public String riderTrips(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Rider rider = userService.getRiderByUsername(userDetails.getUsername());
        List<Trip> trips = tripService.getTripsByRider(rider);
        Optional<Trip> activeTrip = tripService.getActiveTripByRider(rider);
        List<DockingStation> stations = stationService.getActiveStations();

        model.addAttribute("rider", rider);
        model.addAttribute("trips", trips);
        model.addAttribute("activeTrip", activeTrip.orElse(null));
        model.addAttribute("stations", stations);

        return "rider/my-trips";
    }

    // Rider views their invoices
    @GetMapping("/rider/invoices")
    public String riderInvoices(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Rider rider = userService.getRiderByUsername(userDetails.getUsername());
        List<Invoice> invoices = invoiceService.getInvoicesByRider(rider);

        model.addAttribute("rider", rider);
        model.addAttribute("invoices", invoices);

        return "rider/my-invoices";
    }
}
