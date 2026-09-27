package com.ebike.controller;

import com.ebike.model.Vendor;
import com.ebike.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping
    public String listVendors(Model model) {
        List<Vendor> vendors = vendorService.getAllVendors();
        model.addAttribute("vendors", vendors);
        model.addAttribute("newVendor", new Vendor());
        return "vendors";
    }

    @PostMapping("/add")
    public String addVendor(@Valid @ModelAttribute("newVendor") Vendor vendor,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/vendors";
        }
        try {
            vendorService.createVendor(vendor);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vendors";
    }

    @PostMapping("/edit/{id}")
    public String editVendor(@PathVariable Long id,
                             @ModelAttribute Vendor vendor,
                             RedirectAttributes redirectAttributes) {
        try {
            vendorService.updateVendor(id, vendor);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vendors";
    }

    @PostMapping("/delete/{id}")
    public String deleteVendor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vendorService.deleteVendor(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete vendor: " + e.getMessage());
        }
        return "redirect:/vendors";
    }
}
