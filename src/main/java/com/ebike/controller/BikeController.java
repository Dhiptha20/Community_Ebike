package com.ebike.controller;

import com.ebike.dto.BikeDto;
import com.ebike.model.Bike;
import com.ebike.model.BikeStatus;
import com.ebike.model.DockingStation;
import com.ebike.service.BikeService;
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
@RequestMapping("/bikes")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private StationService stationService;

    @GetMapping
    public String listBikes(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "status", required = false) BikeStatus status,
                            Model model) {
        List<Bike> bikes;
        if (keyword != null && !keyword.trim().isEmpty()) {
            bikes = bikeService.searchBikes(keyword);
        } else if (status != null) {
            bikes = bikeService.getAllBikes().stream().filter(b -> b.getStatus() == status).toList();
        } else {
            bikes = bikeService.getAllBikes();
        }

        List<DockingStation> stations = stationService.getAllStations();

        model.addAttribute("bikes", bikes);
        model.addAttribute("stations", stations);
        model.addAttribute("newBike", new BikeDto());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);

        return "bikes";
    }

    @PostMapping("/add")
    public String addBike(@Valid @ModelAttribute("newBike") BikeDto dto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid bike input: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/bikes";
        }
        try {
            bikeService.createBike(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Bike added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bikes";
    }

    @PostMapping("/edit/{id}")
    public String editBike(@PathVariable Long id,
                           @ModelAttribute BikeDto dto,
                           RedirectAttributes redirectAttributes) {
        try {
            bikeService.updateBike(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Bike updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bikes";
    }

    @PostMapping("/delete/{id}")
    public String deleteBike(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bikeService.deleteBike(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bike deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete bike: " + e.getMessage());
        }
        return "redirect:/bikes";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") BikeStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            bikeService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Bike status changed to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bikes";
    }
}
