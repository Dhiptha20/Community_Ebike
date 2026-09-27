package com.ebike.controller;

import com.ebike.dto.PaymentDto;
import com.ebike.model.Invoice;
import com.ebike.model.InvoiceStatus;
import com.ebike.model.Payment;
import com.ebike.model.Role;
import com.ebike.security.CustomUserDetails;
import com.ebike.service.InvoiceService;
import com.ebike.service.PaymentService;
import com.ebike.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listPayments(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Payment> payments;
        Double totalRevenue;

        if (userDetails.getUser().getRole() == Role.ROLE_RIDER) {
            payments = paymentService.getPaymentsByRider(userService.getRiderByUsername(userDetails.getUsername()));
            totalRevenue = paymentService.getTotalSpentByRider(userService.getRiderByUsername(userDetails.getUsername()));
        } else {
            payments = paymentService.getAllPayments();
            totalRevenue = paymentService.getTotalRevenue();
        }

        List<Invoice> pendingInvoices = invoiceService.getPendingInvoices();

        model.addAttribute("payments", payments);
        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        model.addAttribute("pendingInvoices", pendingInvoices);
        model.addAttribute("newPayment", new PaymentDto());

        return "payments";
    }

    @PostMapping("/pay")
    public String processPayment(@Valid @ModelAttribute("newPayment") PaymentDto dto,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment form error: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return redirectUrl != null ? "redirect:" + redirectUrl : "redirect:/payments";
        }

        try {
            Payment payment = paymentService.processPayment(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Payment of ₹" + payment.getAmount() + " processed successfully! Ref: " + payment.getTransactionRef());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment failed: " + e.getMessage());
        }

        if (redirectUrl != null && !redirectUrl.isBlank()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/payments";
    }
}
