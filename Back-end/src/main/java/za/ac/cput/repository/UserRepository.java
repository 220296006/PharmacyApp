package za.ac.cput.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
