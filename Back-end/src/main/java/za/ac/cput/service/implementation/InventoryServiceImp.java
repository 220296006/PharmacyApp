package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Inventory;
import za.ac.cput.model.Medication;
import za.ac.cput.repository.InventoryRepository;
import za.ac.cput.service.InventoryService;

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