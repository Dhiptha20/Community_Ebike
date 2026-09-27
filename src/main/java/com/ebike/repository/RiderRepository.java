package com.ebike.repository;

import com.ebike.model.Rider;
import com.ebike.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByEmail(String email);
    Optional<Rider> findByPhone(String phone);
    Optional<Rider> findByUser(User user);
    Optional<Rider> findByUser_Username(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
