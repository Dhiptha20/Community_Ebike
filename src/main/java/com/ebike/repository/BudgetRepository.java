package com.ebike.repository;

import com.ebike.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findBySector(String sector);

    @Query("SELECT COALESCE(SUM(b.allocatedFund), 0.0) FROM Budget b")
    Double getTotalAllocatedBudget();

    @Query("SELECT COALESCE(SUM(b.actualSpend), 0.0) FROM Budget b")
    Double getTotalActualSpend();
}
