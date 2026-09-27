package com.ebike.repository;

import com.ebike.model.DockingStation;
import com.ebike.model.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DockingStationRepository extends JpaRepository<DockingStation, Long> {
    List<DockingStation> findByStatus(StationStatus status);
}
