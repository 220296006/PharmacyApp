package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Invoice;
import za.ac.cput.repository.InvoiceRepository;
import za.ac.cput.service.InvoiceService;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 18:02
 **/
@Service
@RequiredArgsConstructor
public class InvoiceServiceImp implements InvoiceService {
    private final InvoiceRepository<Invoice> invoiceRepository;
    @Override
    public void createInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public Collection<Invoice> findAllInvoices(String name, int page, int pageSize) {
        return invoiceRepository.list("invoice", 1, 5);
    }

    @Override
    public Invoice findByInvoiceId(Long id) {
        return invoiceRepository.read(id);
    }

    @Override
    public void updateInvoice(Invoice invoice) {
    invoiceRepository.update(invoice);
    }

    @Override
    public boolean deleteInvoice(Long id) {
        invoiceRepository.delete(id);
        return true;
    }
}
