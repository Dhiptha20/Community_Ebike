package com.ebike.controller;

import com.ebike.dto.PurchaseOrderDto;
import com.ebike.model.POStatus;
import com.ebike.model.PurchaseOrder;
import com.ebike.model.Vendor;
import com.ebike.service.PurchaseOrderService;
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
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private VendorService vendorService;

    @GetMapping
    public String listPurchaseOrders(Model model) {
        List<PurchaseOrder> orders = purchaseOrderService.getAllPurchaseOrders();
        List<Vendor> vendors = vendorService.getActiveVendors();

        model.addAttribute("purchaseOrders", orders);
        model.addAttribute("vendors", vendors);
        model.addAttribute("newPo", new PurchaseOrderDto());

        return "purchase-orders";
    }

    @PostMapping("/create")
    public String createPurchaseOrder(@Valid @ModelAttribute("newPo") PurchaseOrderDto dto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/purchase-orders";
        }
        try {
            PurchaseOrder po = purchaseOrderService.createPurchaseOrder(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Purchase Order #" + po.getPoNumber() + " created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/purchase-orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") POStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            PurchaseOrder po = purchaseOrderService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Purchase order #" + po.getPoNumber() + " marked as " + status + (status == POStatus.COMPLETED ? " (Budget spend recorded & journal entry created)" : ""));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/purchase-orders";
    }
}
