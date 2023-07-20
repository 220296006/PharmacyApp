package za.ac.cput.repository;

import za.ac.cput.model.Invoice;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 15:20
 **/
public interface InvoiceRepository <T extends Invoice>{
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(T t);
      void delete(Long id);
}
