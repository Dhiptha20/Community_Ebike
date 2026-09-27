package com.ebike.repository;

import com.ebike.model.RentalTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalTariffRepository extends JpaRepository<RentalTariff, Long> {
    Optional<RentalTariff> findFirstByActiveTrueOrderByCreatedAtDesc();
    List<RentalTariff> findByActive(boolean active);
}
