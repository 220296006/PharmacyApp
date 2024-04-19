package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.exception.ApiException;
import za.ac.cput.exception.ImageUploadException;
import za.ac.cput.model.User;
import za.ac.cput.repository.ImageDataRepository;
import za.ac.cput.service.UserService;
import za.ac.cput.utils.ImageUtils;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/04
 * @Time : 19:44
 **/
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageDataService {
    private final NamedParameterJdbcTemplate jdbc;
    private final ImageDataRepository imageDataRepository;
    private final UserServiceImp userServiceImp;

    public void uploadImage(Long userId, MultipartFile file) {
        log.info("Image uploaded successfully for user with ID {}", userId);
        try {
            log.info("User ID to fetch: {}", userId);
            UserDTO userDTO = userServiceImp.findUserById(userId);
            if (userDTO != null) {
                byte[] compressedImageData = ImageUtils.compressImage(file.getBytes());
                String name = file.getOriginalFilename();
                String type = file.getContentType();

                // Save image data to the image_data table using named parameters
                String sql = "INSERT INTO image_data (user_id, image_data, name, type) " +
                        "VALUES (:userId, :imageData, :name, :type)";
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                params.put("imageData", compressedImageData);
                params.put("name", name);
                params.put("type", type);
                jdbc.update(sql, params);

                // Update user's image URL
                String base64ImageData = Base64.getEncoder().encodeToString(compressedImageData);
                userDTO.setImageUrl("data:image/jpeg;base64," + base64ImageData);

                // Convert UserDTO to User
                User user = convertUserDTOToUser(userDTO);

                // Update user using UserDTO
                userServiceImp.updateUser(userId, userDTO);
            } else {
                log.error("User with ID: {} not found", userId);
                throw new ApiException("User not found with ID " + userId);
            }
        } catch (Exception e) {
            log.error("Error uploading image: {}", e.getMessage());
            throw new ImageUploadException("Error uploading image", e); // Custom API exception
        }
    }

    private User convertUserDTOToUser(UserDTO userDTO) {
        // Perform conversion from UserDTO to User object here
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setImageUrl(userDTO.getImageUrl());
        // Set other properties as needed
        return user;
    }

//    public byte[] downloadImage(Long userId, String fileName) {
//        try {
//            User user = userRepository.read(userId);
//            Optional<ImageData> dbImageData = imageDataRepository.findByNameAndUserId(fileName, userId);
//            if (dbImageData.isPresent()) {
//                return ImageUtils.decompressImage(dbImageData.get().getImageData());
//            } else {
//                throw new ImageNotFoundException("Image not found for user " + userId, fileName); // Custom API exception
//            }
//        } catch (Exception e) {
//            log.error("Error downloading image: {}", e.getMessage());
//            throw new ImageNotFoundException("Error downloading image", e.getMessage()); // Custom API exception
//        }
//    }
}



