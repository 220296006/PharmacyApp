package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
