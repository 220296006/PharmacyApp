package za.ac.cput.repository;
import za.ac.cput.dto.UserUpdateDTO;

import za.ac.cput.model.User;
import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/08
 * @Time : 15:00
 **/
public interface UserRepository <T extends User> {
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(User t);
      void delete(Long id);
      T findUserByEmailIgnoreCase(String email);
      void saveImage(Long userId, byte[] imageData);
}
