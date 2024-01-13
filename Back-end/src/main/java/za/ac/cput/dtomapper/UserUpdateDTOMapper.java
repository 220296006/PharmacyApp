package za.ac.cput.dtomapper;

import org.springframework.beans.BeanUtils;
import za.ac.cput.dto.UserUpdateDTO;
import za.ac.cput.model.User;

public class UserUpdateDTOMapper {
    public static UserUpdateDTO fromUser(User user){
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        BeanUtils.copyProperties(user, userUpdateDTO, "password");
        return userUpdateDTO;
    }

    public static User toUser(UserUpdateDTO UserUpdateDTO){
        User user = new User();
        BeanUtils.copyProperties(UserUpdateDTO, user);
        return user;
    }

}
