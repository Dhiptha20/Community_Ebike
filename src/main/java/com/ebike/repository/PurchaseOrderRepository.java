package com.ebike.repository;

import com.ebike.model.POStatus;
import com.ebike.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(POStatus status);
    List<PurchaseOrder> findAllByOrderByOrderDateDesc();

    @Query("SELECT COALESCE(SUM(po.amount), 0.0) FROM PurchaseOrder po WHERE po.status = com.ebike.model.POStatus.COMPLETED")
    Double getTotalCompletedExpenses();

    @Query("SELECT COALESCE(SUM(po.amount), 0.0) FROM PurchaseOrder po WHERE po.category = :category AND po.status IN (com.ebike.model.POStatus.APPROVED, com.ebike.model.POStatus.COMPLETED)")
    Double getSpendByCategory(String category);
}
