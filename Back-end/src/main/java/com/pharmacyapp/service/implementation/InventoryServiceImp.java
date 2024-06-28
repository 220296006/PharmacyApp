package com.pharmacyapp.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pharmacyapp.model.Inventory;
import com.pharmacyapp.model.Medication;
import com.pharmacyapp.repository.InventoryRepository;
import com.pharmacyapp.service.InventoryService;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 15:26
 **/
@Service
@RequiredArgsConstructor
public class InventoryServiceImp implements InventoryService {
    private final InventoryRepository<Inventory> inventoryRepository;
    @Override
    public void createInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }
    @Override
    public Collection<Inventory> findAllInventory(String name, int page, int pageSize) {
        return inventoryRepository.list("inventory", 1, 50);
    }
    @Override
    public Inventory findByInventoryId(Long id) {
        return inventoryRepository.read(id);
    }
    @Override
    public void updateInventory(Inventory inventory) {
    inventoryRepository.update(inventory);
    }
    @Override
    public boolean deleteInventory(Long id) {
        inventoryRepository.delete(id);
        return true;
    }

    @Override
    public List<Medication> getAvailableMedications(Medication medication) {
        return inventoryRepository.getAvailableMedications();
    }
    }