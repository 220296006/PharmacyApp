package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> { }
