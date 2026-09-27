package com.ebike.controller;

import com.ebike.dto.TelemetryDto;
import com.ebike.model.BatteryTelemetry;
import com.ebike.model.Bike;
import com.ebike.service.BikeService;
import com.ebike.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/telemetry")
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public String telemetryDashboard(Model model) {
        List<BatteryTelemetry> recentTelemetry = telemetryService.getRecentTelemetry();
        List<BatteryTelemetry> lowBatteryAlerts = telemetryService.getLowBatteryAlerts();
        List<Bike> allBikes = bikeService.getAllBikes();

        model.addAttribute("recentTelemetry", recentTelemetry);
        model.addAttribute("lowBatteryAlerts", lowBatteryAlerts);
        model.addAttribute("allBikes", allBikes);
        model.addAttribute("telemetryDto", new TelemetryDto());

        return "telemetry";
    }

    @PostMapping("/simulate")
    public String simulateTelemetry(@Valid @ModelAttribute("telemetryDto") TelemetryDto dto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/telemetry";
        }
        try {
            BatteryTelemetry logged = telemetryService.recordTelemetry(dto);
            if (logged.isLowBatteryAlert()) {
                redirectAttributes.addFlashAttribute("warningMessage",
                        "Telemetry recorded! WARNING: Low battery detected (" + logged.getBatteryLevel() + "%) on Bike " + logged.getBike().getBikeCode() + ". Low-Battery Alert triggered!");
            } else {
                redirectAttributes.addFlashAttribute("successMessage",
                        "Telemetry recorded successfully for Bike " + logged.getBike().getBikeCode() + " (Battery: " + logged.getBatteryLevel() + "%).");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/telemetry";
    }
}
