package za.ac.cput.repository;

import za.ac.cput.model.Prescription;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/11
 * @Time : 20:20
 **/
public interface PrescriptionRepository <T extends Prescription> {
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(T t);
      boolean delete(Long id);

      List<Prescription> findByCustomerId(Long customerId);
}
