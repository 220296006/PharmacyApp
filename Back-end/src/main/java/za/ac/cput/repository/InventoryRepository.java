package za.ac.cput.repository;

import za.ac.cput.model.Inventory;
import za.ac.cput.model.Medication;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 14:53
 **/
public interface InventoryRepository <T extends Inventory> {
     T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(T t);
      void delete(Long id);
      List<Medication> getAvailableMedications();

}

