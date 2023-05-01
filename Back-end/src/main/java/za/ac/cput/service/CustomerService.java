package za.ac.cput.service;
import za.ac.cput.model.Customer;

import java.util.List;

public interface CustomerService extends IService<Customer, Long> {
    List<Customer> getAll();
}
