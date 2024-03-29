package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

import java.io.IOException;
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
    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.save(user));
    }

    @Override
    public Collection<User> getAllUsers(String name, int page, int pageSize) {
        return userRepository.list("users", 1, 50);
    }

    @Override
    public UserUpdateDTO updateAdmin(Long userId, UserUpdateDTO updatedUser) {
        try {
            // Check if the current user has the Admin role
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_USER"))) {
                // If the user has Admin role, proceed with the update
                Optional<User> optionalExistingUser = Optional.ofNullable(userRepository.read(userId));
                if (optionalExistingUser.isPresent()) {
                    User existingUser = getUser(updatedUser, optionalExistingUser);
                    // Save the updated user using the new updateWithDTO method
                    userRepository.update(existingUser);
                    // Return the updated UserUpdateDTO
                    return UserUpdateDTOMapper.fromUser(existingUser);
                } else {
                    // If the user does not exist, return null
                    return null;
                }
            } else {
                // If the user does not have Sys Admin role, throw an exception
                throw new ApiException("Unauthorized: Insufficient permissions to update user with Admin role.");
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user. Please try again.");
        }
    }

    private static User getUser(UserUpdateDTO updatedUser, Optional<User> optionalExistingUser) {
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
        return existingUser;
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

    @Override
    public void saveImage(Long userId,  MultipartFile file) {
        try {
            // Convert MultipartFile to byte array
            byte[] imageData = file.getBytes();
            // Call the repository method to save the image data
            userRepository.saveImage(userId, imageData);
        } catch (IOException exception) {
            log.error("Error reading image file: " + exception.getMessage());
            throw new ApiException("An error occurred while reading the image file.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while uploading user image. Please try again.");
        }
    }
}