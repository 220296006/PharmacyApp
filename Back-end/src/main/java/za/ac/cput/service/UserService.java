package za.ac.cput.service;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.model.User;

/**
 * @author : Thabiso Matsba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:11
 **/

public interface UserService {
    UserDTO createUser(User user);
}
