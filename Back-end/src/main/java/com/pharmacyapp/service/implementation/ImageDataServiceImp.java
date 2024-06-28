package com.pharmacyapp.service.implementation;

import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.exception.ImageNotFoundException;
import com.pharmacyapp.exception.ImageUploadException;
import com.pharmacyapp.model.User;
import com.pharmacyapp.query.UserQuery;
import com.pharmacyapp.repository.ImageDataRepository;
import com.pharmacyapp.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.pharmacyapp.model.ImageData;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

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
                    // Delete the existing image
                    imageDataRepository.deleteById(existingImageData.getId());
                }
                // Save the new image data
                byte[] imageDataBytes = file.getBytes();
                String name = file.getOriginalFilename();
                String type = file.getContentType();

                log.info("Received image data for user ID {}: {} bytes", userId, imageDataBytes.length);

                // Create and persist new image data
                ImageData image = new ImageData();
                image.setName(name);
                image.setType(type);
                image.setImageData(imageDataBytes);
                image.setUserId(userId); // Set the user in the image data

                imageDataRepository.save(image);

                // Generate unique image ID
                Long generatedImageId = generateUniqueImageId();

                // Update user's image URL in the Users table
                String imageUrl = generateImageUrl(generatedImageId);
                MapSqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("imageUrl", imageUrl);
                jdbc.update(UserQuery.UPDATE_USER_PROFILE_IMAGE_SQL, parameters);

                // Set imageUrl in User object
                log.info("Fetching user Image URL: {}", imageUrl);
                user.setImageUrl(imageUrl);
               // user.setImageData(image);

                // Fetch the image data associated with the user
                byte[] userImageData = image.getImageData();

                // Update user in the database
                userServiceImp.updateUserImageUrl(userId, imageUrl);

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
       log.info("Attempting to delete image data for user ID: {}", userId);
       User user = userServiceImp.findUserById(userId);
       if (user != null) {
           ImageData imageData = user.getImageData();
           if (imageData != null) {
               log.info("Found image data for user ID: {}. Deleting...", userId);
               imageDataRepository.deleteById(imageData.getId());
               log.info("Successfully deleted image data for user ID: {}", userId);
           } else {
               log.warn("No image data found for user ID: {}", userId);
           }
       }
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



