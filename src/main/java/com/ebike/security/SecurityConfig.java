package com.ebike.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                // Disable CSRF on REST API paths to allow seamless Postman testing
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/**"))
            )
            .authorizeHttpRequests(auth -> auth
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // Public authentication and home
                .requestMatchers("/", "/login", "/register").permitAll()
                // Open REST APIs for evaluation/Postman
                .requestMatchers("/api/**").permitAll()
                // Admin routes
                .requestMatchers("/bikes/**", "/stations/**", "/telemetry/**", "/riders/**").hasRole("ADMIN")
                // Invoicing & Financial routes
                .requestMatchers("/invoicing/**", "/purchase-orders/**", "/vendors/**", "/budgets/**", "/accounts/**").hasAnyRole("ADMIN", "INVOICING_USER")
                // Invoices & Payments: shared between Admin, Invoicing User and Rider
                .requestMatchers("/invoices/**", "/payments/**").hasAnyRole("ADMIN", "INVOICING_USER", "RIDER")
                // Rider specific routes
                .requestMatchers("/rider/**").hasRole("RIDER")
                // Reports & Dashboards
                .requestMatchers("/reports/**").hasAnyRole("ADMIN", "INVOICING_USER")
                .requestMatchers("/dashboard").hasAnyRole("ADMIN", "INVOICING_USER", "RIDER")
                .requestMatchers("/profile").authenticated()
                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Enable HTTP Basic for REST APIs testing in Postman
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
