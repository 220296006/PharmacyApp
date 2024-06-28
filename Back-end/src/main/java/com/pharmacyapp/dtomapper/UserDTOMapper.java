package com.pharmacyapp.dtomapper;

import com.pharmacyapp.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import com.pharmacyapp.dto.UserDTO;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 17:13
 **/
@Component
public class UserDTOMapper {
    public static UserDTO fromUser(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO, "password");
        return userDTO;
    }

      public static User toUser(User userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
