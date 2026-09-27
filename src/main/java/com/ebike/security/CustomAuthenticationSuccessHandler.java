package com.ebike.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                response.sendRedirect("/dashboard");
                return;
            } else if ("ROLE_INVOICING_USER".equals(role)) {
                response.sendRedirect("/invoicing/dashboard");
                return;
            } else if ("ROLE_RIDER".equals(role)) {
                response.sendRedirect("/rider/dashboard");
                return;
            }
        }

        response.sendRedirect("/dashboard");
    }
}
