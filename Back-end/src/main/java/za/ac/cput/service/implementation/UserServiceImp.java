package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.dto.UserUpdateDTO;
import za.ac.cput.dtomapper.UserDTOMapper;
import za.ac.cput.dtomapper.UserUpdateDTOMapper;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Confirmation;
import za.ac.cput.model.User;
import za.ac.cput.repository.ConfirmationRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;

import java.util.Collection;
import java.util.Optional;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:11
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {
    private final UserRepository<User> userRepository;
    private final ConfirmationRepository<Confirmation> confirmationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(User user) {

        return UserDTOMapper.fromUser(userRepository.save(user));
    }

    @Override
    public Collection<User> getAllUsers(String name, int page, int pageSize) {

        return userRepository.list("users", 1, 10);
    }

    @Override
    public UserUpdateDTO updateUserById(Long id, UserUpdateDTO updatedUser) {
        try {
            Optional<User> optionalExistingUser = Optional.ofNullable(userRepository.read(id));

            if (optionalExistingUser.isPresent()) {
                User existingUser = optionalExistingUser.get();

                // Update the existing user with the new data
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setMiddleName(updatedUser.getMiddleName());

                // Update the email address if provided
                if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    existingUser.setEmail(updatedUser.getEmail());
                }

                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setPhone(updatedUser.getPhone());

                // Update the password only if provided
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                // Save the updated user
                User updatedUserEntity = userRepository.update(existingUser);

                // Convert the updated user to DTO for response
                return UserUpdateDTOMapper.fromUser(updatedUserEntity);
            } else {
                // If the user does not exist, return null
                return null;
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user. Please try again.");
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
