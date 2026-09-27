package com.ebike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "riders")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, nullable = true)
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    private User user;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Rider() {}

    public Rider(User user, String name, String email, String phone, String status) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status != null ? status : "ACTIVE";
    }

    // Getters and Setters
    public Long getRiderId() {
        return riderId;
    }
    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
