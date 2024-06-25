package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.dtomapper.UserDTOMapper;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.User;
import za.ac.cput.repository.ElasticsearchUserRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

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
    private final ElasticsearchUserRepository elasticsearchUserRepository;


    @Override
    public UserDTO createUser(User user) {
        User created = userRepository.save(UserDTOMapper.toUser(user));
        elasticsearchUserRepository.save(created);
        return UserDTOMapper.fromUser(created);
    }

    @Override
    public Collection<User> getAllUsers(String name, int page, int pageSize) {
        elasticsearchUserRepository.findAll();
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
            elasticsearchUserRepository.save(updated);
            return UserDTOMapper.fromUser(updated);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the user. Please try again.");
        }
    }

    @Override
    public User findUserById(Long id) {
        elasticsearchUserRepository.findById(id);
        return UserDTOMapper.toUser(userRepository.read(id));
    }


    @Override
    public boolean deleteUser(Long id) {
        userRepository.delete(id);
        elasticsearchUserRepository.deleteById(id);
        return true;
    }


    @Override
    public User findUserByEmailIgnoreCase(String email) {
        elasticsearchUserRepository.findByEmail(email);
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public List<User> searchByFirstName(String firstName) {
        return elasticsearchUserRepository.findByFirstName(firstName);
    }

    public List<User> searchByMiddleName(String middleName) {
        return elasticsearchUserRepository.findByMiddleName(middleName);
    }

    public List<User> searchByLastName(String lastName) {

        return elasticsearchUserRepository.findByLastName(lastName);
    }

    public List<User> searchByEmail(String email) {
        return elasticsearchUserRepository.findByEmail(email);
    }

    public List<User> searchByAddress(String address) {

        return elasticsearchUserRepository.findByAddress(address);
    }

    public List<User> searchByPhone(String phone) {

        return elasticsearchUserRepository.findByPhone(phone);
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
    public Integer getUserCount() {
        return userRepository.countUsers();
    }

    @Override
    public void updateUserImageUrl(Long userId, String imageUrl) {
        User user = userRepository.read(userId);
        user.setImageUrl(imageUrl);
        userRepository.updateUserImageUrl(userId, imageUrl);
    }

}