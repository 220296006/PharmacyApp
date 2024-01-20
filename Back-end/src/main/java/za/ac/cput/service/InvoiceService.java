package za.ac.cput.service;

import za.ac.cput.model.Invoice;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 17:58
 **/
public interface InvoiceService {
    void createInvoice(Invoice invoice);
    Collection<Invoice> findAllInvoices(String name, int page, int pageSize);
    Invoice findByInvoiceId(Long id);
    void updateInvoice(Invoice invoice);
    boolean deleteInvoice(Long id);
    List<Invoice> getInvoicesByCustomerId(Long customerId);

    long countInvoices();
    BigInteger getTotalBilledAmount();


}
