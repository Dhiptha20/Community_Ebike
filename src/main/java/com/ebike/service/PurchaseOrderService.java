package com.ebike.service;

import com.ebike.dto.PurchaseOrderDto;
import com.ebike.exception.BadRequestException;
import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.POStatus;
import com.ebike.model.PurchaseOrder;
import com.ebike.model.Vendor;
import com.ebike.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private AccountingService accountingService;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAllByOrderByOrderDateDesc();
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto dto) {
        Vendor vendor = vendorService.getVendorById(dto.getVendorId());

        PurchaseOrder po = new PurchaseOrder();
        po.setVendor(vendor);
        po.setDescription(dto.getDescription().trim());
        po.setAmount(dto.getAmount());
        po.setOrderDate(LocalDate.now());
        po.setStatus(POStatus.CREATED);
        po.setCategory(dto.getCategory() != null ? dto.getCategory().trim() : "Fleet Maintenance");
        po.setPoNumber("PO-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000));

        return purchaseOrderRepository.save(po);
    }

    @Transactional
    public PurchaseOrder updateStatus(Long id, POStatus newStatus) {
        PurchaseOrder po = getPurchaseOrderById(id);

        if (po.getStatus() == POStatus.COMPLETED) {
            throw new BadRequestException("Purchase order is already completed and cannot change status.");
        }

        po.setStatus(newStatus);
        PurchaseOrder savedPo = purchaseOrderRepository.save(po);

        // When a purchase order is completed, update the budget spend and record journal entry
        if (newStatus == POStatus.COMPLETED) {
            String category = po.getCategory() != null ? po.getCategory() : "Fleet Maintenance";
            budgetService.recordSpend(category, po.getAmount());

            // Double entry:
            // Debit: Maintenance Expense (5010)
            // Credit: Cash/Bank (1010)
            accountingService.recordTransaction(
                    "5010",
                    "1010",
                    po.getAmount(),
                    "Vendor maintenance PO " + po.getPoNumber() + " - " + po.getDescription(),
                    po.getPoNumber()
            );
        }

        return savedPo;
    }
}
