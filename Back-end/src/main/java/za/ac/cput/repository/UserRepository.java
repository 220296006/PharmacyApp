package za.ac.cput.repository;
import za.ac.cput.model.User;
import java.util.Collection;

public interface UserRepository <T extends User> {
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);

      T read(Long id);

      T update(T t);

      boolean delete(Long id);
}
