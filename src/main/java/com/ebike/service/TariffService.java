package com.ebike.service;

import com.ebike.model.RentalTariff;
import com.ebike.repository.RentalTariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TariffService {

    @Autowired
    private RentalTariffRepository tariffRepository;

    public RentalTariff getActiveTariff() {
        return tariffRepository.findFirstByActiveTrueOrderByCreatedAtDesc()
                .orElseGet(() -> {
                    // Fallback default tariff if none active
                    RentalTariff defaultTariff = new RentalTariff("Standard City Tariff", 10.0, 2.0, 5.0, true);
                    return tariffRepository.save(defaultTariff);
                });
    }

    public List<RentalTariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    @Transactional
    public RentalTariff saveTariff(RentalTariff tariff) {
        if (tariff.isActive()) {
            // Deactivate other tariffs
            List<RentalTariff> existing = tariffRepository.findByActive(true);
            for (RentalTariff t : existing) {
                t.setActive(false);
                tariffRepository.save(t);
            }
        }
        return tariffRepository.save(tariff);
    }

    public Double calculateFare(long durationMinutes, double distanceKm) {
        RentalTariff activeTariff = getActiveTariff();
        return activeTariff.calculateFare(durationMinutes, distanceKm);
    }
}
