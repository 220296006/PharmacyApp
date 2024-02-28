package za.ac.cput.service;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.dto.UserUpdateDTO;
import za.ac.cput.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * @author : Thabiso Matsba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:11
 **/

public interface UserService {
    UserDTO createUser(User user);

    Collection<User> getAllUsers(String name, int page, int pageSize);

    UserUpdateDTO updateSysAdmin(Long id, UserUpdateDTO updatedUser);

    User findUserById(Long id);

    boolean deleteUser(Long id);
    Boolean verifyToken(String token);
    User findUserByEmailIgnoreCase(String email);
}
