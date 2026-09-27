package com.ebike.controller;

import com.ebike.dto.RiderRegistrationDto;
import com.ebike.model.Role;
import com.ebike.model.User;
import com.ebike.repository.UserRepository;
import com.ebike.security.CustomUserDetails;
import com.ebike.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("riderDto")) {
            model.addAttribute("riderDto", new RiderRegistrationDto());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerRider(@Valid @ModelAttribute("riderDto") RiderRegistrationDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.riderDto", bindingResult);
            redirectAttributes.addFlashAttribute("riderDto", dto);
            return "redirect:/register";
        }

        try {
            userService.registerRider(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You can now log in.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("riderDto", dto);
            return "redirect:/register";
        }
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(userDetails.getId()).orElse(userDetails.getUser());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @ModelAttribute("user") User formUser,
                                RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(userDetails.getId()).orElse(null);
        if (user != null) {
            user.setFullName(formUser.getFullName());
            user.setPhone(formUser.getPhone());
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        }
        return "redirect:/profile";
    }
}
