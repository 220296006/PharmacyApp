package com.pharmacyapp.repository;

import com.pharmacyapp.model.Inventory;
import com.pharmacyapp.model.Medication;

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

