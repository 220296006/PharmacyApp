package za.ac.cput.service.implementation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Inventory;
import za.ac.cput.repository.InventoryRepository;
import za.ac.cput.service.InventoryService;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class InventoryServiceImp implements InventoryService {

    private final InventoryRepository inventoryRepository;
    @Override
    public Inventory save(Inventory inventory) {
        log.info("Saving Inventory:{}", inventory);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory read(Long s) {
        log.info("Read Inventory By ID:{}", s);
        return inventoryRepository.findById(s).get();
    }

    @Override
    public Inventory update(Inventory inventory) {
        log.info("Updating Inventory:{}", inventory);
        return inventoryRepository.save(inventory);
    }

    @Override
    public boolean delete(Long s) {
        log.info("Deleting Inventory By ID:{}", s);
        inventoryRepository.deleteById(s);
        return true;
    }

    @Override
    public List<Inventory> getAll() {
        log.info("Get All Inventory:");
        return inventoryRepository.findAll();
    }
}
