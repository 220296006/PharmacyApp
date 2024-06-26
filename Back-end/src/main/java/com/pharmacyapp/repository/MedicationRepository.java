package com.pharmacyapp.repository;

import com.pharmacyapp.model.Medication;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 00:57
 **/
public interface MedicationRepository <T extends Medication> {
     T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(T t);
      boolean delete(Long id);
      List<Medication> findByPrescriptionId(Long prescription_id);
     Integer medicationCount();
}
