package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.exception.ApiException;
import za.ac.cput.exception.ImageUploadException;
import za.ac.cput.model.User;
import za.ac.cput.repository.ImageDataRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.utils.ImageUtils;

import javax.transaction.Transactional;
import java.io.IOException;
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
    private final UserRepository<User> userRepository;

    public void uploadImage(Long userId, MultipartFile file) throws IOException {
        log.info("Image uploaded successfully for user with ID {}", userId);
        try {
            User user = userRepository.read(userId);
            if (user != null) {
                byte[] compressedImageData = ImageUtils.compressImage(file.getBytes());
                String name = file.getOriginalFilename();
                String type = file.getContentType();
                // Save image data to the image_data table using named parameters
                String sql = "UPDATE Users SET image_data = :imageData, name = :name, type = :type WHERE id = :userId";
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                params.put("imageData", compressedImageData);
                params.put("name", name);
                params.put("type", type);
                jdbc.update(sql, params);
                // Update user's image URL
                String base64ImageData = Base64.getEncoder().encodeToString(compressedImageData);
                user.setImageUrl("data:image/jpeg;base64," + base64ImageData);
                userRepository.update(user);
            } else {
                log.error("User with ID: {} not found", userId);
                throw new ApiException("User not found with ID " + userId);
            }
        } catch (Exception e) {
            log.error("Error uploading image: {}", e.getMessage());
            throw new ImageUploadException("Error uploading image", e); // Custom API exception
        }
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



