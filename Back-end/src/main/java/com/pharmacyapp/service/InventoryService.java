package com.pharmacyapp.service;

import com.pharmacyapp.model.Inventory;
import com.pharmacyapp.model.Medication;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 15:24
 **/
public interface InventoryService {
    void createInventory(Inventory inventory);
    Collection<Inventory> findAllInventory (String name, int page, int pageSize);
    Inventory findByInventoryId(Long id);
    void updateInventory(Inventory inventory);
    boolean deleteInventory(Long id);

    List<Medication> getAvailableMedications(Medication medication);
}
