package com.pharmacyapp.repository;

import com.pharmacyapp.model.Invoice;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

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

      List<Invoice> findInvoiceByCustomerId(Long customerId);

      Integer countInvoices();
      BigInteger getTotalBilledAmount();

}
