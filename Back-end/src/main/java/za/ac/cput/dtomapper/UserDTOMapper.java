package za.ac.cput.dtomapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.model.User;

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
        BeanUtils.copyProperties(user, userDTO);
        user.setPassword(null);
        return userDTO;
    }

      public static User toUser(User userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
