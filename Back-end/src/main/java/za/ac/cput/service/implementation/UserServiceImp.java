package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.dtomapper.UserDTOMapper;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Confirmation;
import za.ac.cput.model.User;
import za.ac.cput.repository.ConfirmationRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:11
 **/
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImp implements UserService {
    private final UserRepository<User> userRepository;
    private final ConfirmationRepository<Confirmation> confirmationRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.save(user));
    }

    @Override
    public Collection<User> getAllUsers(String name, int page, int pageSize) {
        return userRepository.list("users", 1, 50);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO updatedUser) {
        try {
            User user = userRepository.read(userId);
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setMiddleName(updatedUser.getMiddleName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            User updated = userRepository.update(user);
            return convertToDto(updated);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user. Please try again.");
        }
    }
    // Helper method to convert User entity to UserDTO
    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setImageUrl(user.getImageUrl());
        userDTO.setImageData(user.getImageData());
        return userDTO;
    }


    @Override
    public User findUserById(Long id) {
        return UserDTOMapper.toUser(userRepository.read(id));
    }


    @Override
    public boolean deleteUser(Long id) {
        userRepository.delete(id);
        return true;
    }


    @Override
    public User findUserByEmailIgnoreCase(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    @Override
    public UserDTO getUserInfo(String username) {
        User user = userRepository.findUserByEmailIgnoreCase(username);
        if (user != null) {
            return UserDTOMapper.fromUser(user);
        }
        return null;
    }

}