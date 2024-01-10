package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.dtomapper.UserDTOMapper;
import za.ac.cput.model.Confirmation;
import za.ac.cput.model.User;
import za.ac.cput.repository.ConfirmationRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:11
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository<User> userRepository;
    private final ConfirmationRepository<Confirmation> confirmationRepository;

    @Override
    public UserDTO createUser(User user) {

        return UserDTOMapper.fromUser(userRepository.save(user));
    }

    @Override
    public Collection<User> getAllUsers(String name, int page, int pageSize) {

        return userRepository.list("users", 1, 10);
    }

    @Override
    public UserDTO updateUserById(Long id , User updatedUser) {

        User existingUser = userRepository.findById(id);

        // Check if the user exists
        if (existingUser != null) {
            // Update the existing user with the new data
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setMiddleName(updatedUser.getMiddleName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setPhone(updatedUser.getPhone());
            // Save the updated user
            User updatedUserEntity = userRepository.save(existingUser);
            // Convert the updated user to DTO for response
            return UserDTOMapper.fromUser(updatedUserEntity);
        } else {
            // If the user does not exist, return null
            return null;
        }
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
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.findByToken(token);
        return Boolean.TRUE;
    }

}
