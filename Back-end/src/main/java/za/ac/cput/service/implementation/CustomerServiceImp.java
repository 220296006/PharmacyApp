package za.ac.cput.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Customer;
import za.ac.cput.repository.CustomerRepository;
import za.ac.cput.service.CustomerService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        log.info("Saving Customer:{}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Collection<Customer> list(int page, int pageSize) {
                log.info("Get All Customers");

        return null;
    }

    @Override
    public Customer read(Long s) {
        log.info("Reading Customer By ID:{}", s);
        if (customerRepository.findById(s).isPresent()) {
            return customerRepository.findById(s).get();
        } else return customerRepository.findById(s).orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        log.info("Updating Customer:{}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public boolean delete(Long s) {
        log.info("Deleting Customer by ID:{}", s);
        customerRepository.deleteById(s);
        return true;
    }
}
