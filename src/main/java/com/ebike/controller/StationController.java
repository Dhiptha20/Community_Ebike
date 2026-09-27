package com.ebike.controller;

import com.ebike.dto.StationDto;
import com.ebike.model.DockingStation;
import com.ebike.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping
    public String listStations(Model model) {
        List<DockingStation> stations = stationService.getAllStations();
        model.addAttribute("stations", stations);
        model.addAttribute("newStation", new StationDto());
        return "stations";
    }

    @PostMapping("/add")
    public String addStation(@Valid @ModelAttribute("newStation") StationDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/stations";
        }
        try {
            stationService.createStation(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Docking station added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/stations";
    }

    @PostMapping("/edit/{id}")
    public String editStation(@PathVariable Long id,
                              @ModelAttribute StationDto dto,
                              RedirectAttributes redirectAttributes) {
        try {
            stationService.updateStation(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Docking station updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/stations";
    }

    @PostMapping("/delete/{id}")
    public String deleteStation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            stationService.deleteStation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Docking station deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete station: " + e.getMessage());
        }
        return "redirect:/stations";
    }
}
