package com.ebike.service;

import com.ebike.exception.ResourceNotFoundException;
import com.ebike.model.Vendor;
import com.ebike.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public List<Vendor> getActiveVendors() {
        return vendorRepository.findByStatus("ACTIVE");
    }

    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + id));
    }

    @Transactional
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Transactional
    public Vendor updateVendor(Long id, Vendor updated) {
        Vendor vendor = getVendorById(id);
        vendor.setVendorName(updated.getVendorName());
        vendor.setContactPerson(updated.getContactPerson());
        vendor.setEmail(updated.getEmail());
        vendor.setPhone(updated.getPhone());
        vendor.setAddress(updated.getAddress());
        vendor.setStatus(updated.getStatus());
        return vendorRepository.save(vendor);
    }

    @Transactional
    public void deleteVendor(Long id) {
        Vendor vendor = getVendorById(id);
        vendorRepository.delete(vendor);
    }
}
