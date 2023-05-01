package za.ac.cput.service;
import za.ac.cput.model.Inventory;

import java.util.List;

public interface InventoryService extends IService<Inventory, Long>{

List<Inventory> getAll();

}
