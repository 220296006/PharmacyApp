package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.exception.ImageNotFoundException;
import za.ac.cput.exception.ImageUploadException;
import za.ac.cput.model.ImageData;
import za.ac.cput.model.User;
import za.ac.cput.repository.ImageDataRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.utils.ImageUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

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

    private final ImageDataRepository imageDataRepository;
    private final UserRepository<User> userRepository;

    public void uploadImage(Long userId, MultipartFile file) throws IOException {
        try {
            User user = userRepository.read(userId);
            ImageData imageData = imageDataRepository.save(ImageData.builder()
                    .user(user)
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build());
            log.info("Image uploaded successfully for user with ID {}", userId);
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



