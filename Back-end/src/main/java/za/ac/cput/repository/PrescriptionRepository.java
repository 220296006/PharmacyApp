package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
