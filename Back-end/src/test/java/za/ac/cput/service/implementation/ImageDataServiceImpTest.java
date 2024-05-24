package za.ac.cput.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.ImageData;
import za.ac.cput.model.User;
import za.ac.cput.repository.ImageDataRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageDataServiceImpTest {

    @Mock
    private ImageDataRepository imageDataRepository;

    @Mock
    private UserServiceImp userServiceImp;

    @InjectMocks
    private ImageDataServiceImp imageDataServiceImp;

    private MultipartFile multipartFile;

    @BeforeEach
    public void setup() {
        multipartFile = mock(MultipartFile.class);
    }

    @Test
    void uploadImage() throws IOException {
        Long userId = 1L;
        User user = new User();
        when(userServiceImp.findUserById(userId)).thenReturn(user);
        when(multipartFile.getBytes()).thenReturn(new byte[10]);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(imageDataRepository.findByUserId(userId)).thenReturn(Optional.empty());

        imageDataServiceImp.uploadImage(userId, multipartFile);

        verify(imageDataRepository, times(1)).save(any(ImageData.class));
    }

    @Test
    void getImageData() {
        Long userId = 1L;
        when(userServiceImp.findUserById(userId)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> {
            imageDataServiceImp.uploadImage(userId, multipartFile);
        });

        assertEquals("User not found with ID " + userId, exception.getMessage());
        verify(imageDataRepository, times(0)).delete(any(ImageData.class));
        verify(imageDataRepository, times(0)).save(any(ImageData.class));
    }

//    @Test
//    void deleteImage() {
//        Long userId = 1L;
//        User user = new User();
//        ImageData imageData = new ImageData();
//        user.setImageData(imageData);
//        when(userServiceImp.findUserById(userId)).thenReturn(user);
//        when(imageDataRepository.findByUserId(userId)).thenReturn(Optional.of(imageData));
//
//        imageDataServiceImp.deleteImage(userId);
//
//        verify(imageDataRepository, times(1)).delete(imageData);
//    }
}