package com.ebike.controller;

import com.ebike.model.Invoice;
import com.ebike.model.InvoiceStatus;
import com.ebike.model.Role;
import com.ebike.security.CustomUserDetails;
import com.ebike.service.InvoiceService;
import com.ebike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listInvoices(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Invoice> invoices;
        if (userDetails.getUser().getRole() == Role.ROLE_RIDER) {
            invoices = invoiceService.getInvoicesByRider(userService.getRiderByUsername(userDetails.getUsername()));
        } else {
            invoices = invoiceService.getAllInvoices();
        }

        model.addAttribute("invoices", invoices);
        return "invoices";
    }

    @PostMapping("/{id}/status")
    public String updateInvoiceStatus(@PathVariable Long id,
                                      @RequestParam("status") InvoiceStatus status,
                                      RedirectAttributes redirectAttributes) {
        try {
            invoiceService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Invoice status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/invoices";
    }
}
