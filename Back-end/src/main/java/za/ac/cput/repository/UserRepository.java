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
      T saveAdmin(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(User t);
      void delete(Long id);
      T findUserByEmailIgnoreCase(String email);
      Boolean existByEmail(String email);
      UserUpdateDTO updateSysAdmin(UserUpdateDTO updatedUser);

    List<User> getUsersByRole(String roleName);

      // Additional methods for managing roles and permissions
      void assignRole(Long userId, String roleName);

      void revokeRole(Long userId, String roleName);
}
