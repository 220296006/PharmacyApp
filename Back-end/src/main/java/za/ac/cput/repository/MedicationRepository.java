package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.model.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
