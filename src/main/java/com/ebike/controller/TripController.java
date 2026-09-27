package com.ebike.controller;

import com.ebike.dto.TripEndDto;
import com.ebike.dto.TripStartDto;
import com.ebike.model.DockingStation;
import com.ebike.model.Rider;
import com.ebike.model.Trip;
import com.ebike.security.CustomUserDetails;
import com.ebike.service.StationService;
import com.ebike.service.TripService;
import com.ebike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @Autowired
    private StationService stationService;

    @GetMapping
    public String listTrips(Model model) {
        List<Trip> trips = tripService.getAllTrips();
        List<DockingStation> stations = stationService.getAllStations();
        model.addAttribute("trips", trips);
        model.addAttribute("stations", stations);
        return "trips";
    }

    @PostMapping("/unlock")
    public String unlockBike(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam("bikeId") Long bikeId,
                             @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                             RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        try {
            Rider rider = userService.getRiderByUsername(userDetails.getUsername());
            TripStartDto dto = new TripStartDto();
            dto.setRiderId(rider.getRiderId());
            dto.setBikeId(bikeId);

            tripService.unlockBikeAndStartTrip(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Bike unlocked successfully! Have a safe and green ride.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (redirectUrl != null && !redirectUrl.isBlank()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/rider/dashboard";
    }

    @PostMapping("/complete")
    public String completeTrip(@RequestParam("tripId") Long tripId,
                               @RequestParam(value = "endStationId", required = false) Long endStationId,
                               @RequestParam(value = "distanceKm", defaultValue = "2.5") Double distanceKm,
                               @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                               RedirectAttributes redirectAttributes) {
        try {
            TripEndDto dto = new TripEndDto();
            dto.setTripId(tripId);
            dto.setEndStationId(endStationId);
            dto.setDistanceKm(distanceKm);

            Trip completedTrip = tripService.completeTrip(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Trip #" + completedTrip.getTripId() + " completed successfully! Fare: ₹" + completedTrip.getFare() + " (Invoice generated)");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error ending trip: " + e.getMessage());
        }

        if (redirectUrl != null && !redirectUrl.isBlank()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/rider/dashboard";
    }
}
