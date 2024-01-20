package za.ac.cput.repository;

import za.ac.cput.model.Customer;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 17:57
 **/
public interface CustomerRepository <T extends Customer> {
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(T t);
      void delete(Long id);

      long count();}
