package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.model.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
