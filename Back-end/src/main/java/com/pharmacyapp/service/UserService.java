package com.pharmacyapp.service;
import com.pharmacyapp.dto.UserDTO;
import com.pharmacyapp.model.User;

import java.util.Collection;

/**
 * @author : Thabiso Matsba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:11
 **/

public interface UserService {
    UserDTO createUser(User user);

    Collection<User> getAllUsers(String name, int page, int pageSize);

    UserDTO updateUser(Long id, UserDTO updatedUser);

    User findUserById(Long id);

    boolean deleteUser(Long id);

    User findUserByEmailIgnoreCase(String email);

    UserDTO getUserInfo(String username);

    Integer getUserCount();

    void updateUserImageUrl(Long userId, String imageUrl);
}
