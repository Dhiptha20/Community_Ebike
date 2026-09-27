package com.ebike.service;

import com.ebike.dto.RiderRegistrationDto;
import com.ebike.exception.BadRequestException;
import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.Rider;
import com.ebike.model.Role;
import com.ebike.model.User;
import com.ebike.repository.RiderRepository;
import com.ebike.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User registerRider(RiderRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username is already taken: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email is already registered: " + dto.getEmail());
        }
        if (riderRepository.existsByPhone(dto.getPhone())) {
            throw new BadRequestException("Phone number is already registered: " + dto.getPhone());
        }

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setFullName(dto.getFullName().trim());
        user.setPhone(dto.getPhone().trim());
        user.setRole(Role.ROLE_RIDER);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Rider rider = new Rider();
        rider.setUser(savedUser);
        rider.setName(dto.getFullName().trim());
        rider.setEmail(dto.getEmail().trim().toLowerCase());
        rider.setPhone(dto.getPhone().trim());
        rider.setStatus("ACTIVE");
        riderRepository.save(rider);

        return savedUser;
    }

    public User createUser(String username, String password, String email, String fullName, String phone, Role role) {
        if (userRepository.existsByUsername(username)) {
            return userRepository.findByUsername(username).orElse(null);
        }
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email.trim().toLowerCase());
        user.setFullName(fullName != null ? fullName.trim() : username);
        user.setPhone(phone);
        user.setRole(role);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public Rider getRiderByUsername(String username) {
        return riderRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Rider profile not found for user: " + username));
    }

    public List<Rider> findAllRiders() {
        return riderRepository.findAll();
    }
}
