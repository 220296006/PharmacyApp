package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.exception.ApiException;
import za.ac.cput.exception.ImageNotFoundException;
import za.ac.cput.exception.ImageUploadException;
import za.ac.cput.model.ImageData;
import za.ac.cput.model.User;
import za.ac.cput.repository.ImageDataRepository;
import za.ac.cput.utils.ImageUtils;

import javax.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

import static za.ac.cput.query.UserQuery.UPDATE_USER_PROFILE_IMAGE_SQL;

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
public class ImageDataServiceImp {
    private final NamedParameterJdbcTemplate jdbc;
    private final ImageDataRepository imageDataRepository;
    private final UserServiceImp userServiceImp;

    public void uploadImage(Long userId, MultipartFile file) {
        log.info("Image uploaded successfully for user with ID {}", userId);
        try {
            log.info("Fetching user with ID: {}", userId);
            User user = userServiceImp.findUserById(userId);
            if (user != null) {
                // Check if the user already has an image
                ImageData existingImageData = user.getImageData();
                if (existingImageData != null) {
                    // Delete the existing image before uploading a new one
                    imageDataRepository.delete(existingImageData);
                }

                byte[] imageDataBytes = file.getBytes();
                String name = file.getOriginalFilename();
                String type = file.getContentType();

                log.info("Received image data for user ID {}: {} bytes", userId, imageDataBytes.length);

                // Persist image data
                ImageData imageData = new ImageData();
                imageData.setImageData(imageDataBytes);
                imageData.setName(name);
                imageData.setType(type);
                imageData.setUserId(userId);
                imageDataRepository.save(imageData);

                // Generate unique image ID
                Long generatedImageId = generateUniqueImageId();

                // Update user's image URL in the Users table
                String imageUrl = generateImageUrl(generatedImageId);
                MapSqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("imageUrl", imageUrl);
                jdbc.update(UPDATE_USER_PROFILE_IMAGE_SQL, parameters);

                // Set imageUrl in User object
                log.info("Fetching user Image URL: {}", imageUrl);
                user.setImageUrl(imageUrl);

                // Fetch the image data associated with the user
                byte[] userImageData = imageData.getImageData();

                // Update user in the database
                userServiceImp.updateUser(userId, convertToDto(user));

                // Log and return success message
                log.info("Updated User: {}", user);
            } else {
                log.error("User with ID: {} not found", userId);
                throw new ApiException("User not found with ID " + userId);
            }
        } catch (Exception e) {
            log.error("Error uploading image: {}", e.getMessage());
            throw new ImageUploadException("Error uploading image", e);
        }
    }


    // Generate unique image ID (You can use a different approach if needed)
    private Long generateUniqueImageId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    // Generate image URL including image data query parameter
    private String generateImageUrl(Long imageId) {
        return "https://pharmacyapp.com/images/" + imageId + "?includeData=true";
    }

    // Fetch image data based on user ID
    public byte[] getImageData(Long id) {
        log.info("Fetching image data for user ID: {}", id);
        Optional<ImageData> imageDataOptional = imageDataRepository.findByUserId(id);
        byte[] imageData = imageDataOptional.map(ImageData::getImageData).orElse(null);
        if (imageData != null) {
            log.info("Retrieved image data for user ID {}: {} bytes", id, imageData.length);
        } else {
            log.info("No image data found for user ID: {}", id);
        }
        return imageData;
    }

   // Delete Image
    public void deleteImage(Long userId) {
        log.info("Deleting image data for user ID: {}", userId);
        User user = userServiceImp.findUserById(userId);
        ImageData imageData = user.getImageData();
        if (imageData != null) {
            imageDataRepository.delete(imageData);
        }
    }


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
        log.info("UserDTO: {}", userDTO );
        return userDTO;

    }

    public byte[] downloadImage(Long userId, String fileName) {
        try {
            User user = userServiceImp.findUserById(userId);
            Optional<ImageData> dbImageData = imageDataRepository.findByNameAndUserId(fileName, userId);
            if (dbImageData.isPresent()) {
                return ImageUtils.decompressImage(dbImageData.get().getImageData());
            } else {
                throw new ImageNotFoundException("Image not found for user " + userId, fileName); // Custom API exception
            }
        } catch (Exception e) {
            log.error("Error downloading image: {}", e.getMessage());
            throw new ImageNotFoundException("Error downloading image", e.getMessage()); // Custom API exception
        }
    }
}



