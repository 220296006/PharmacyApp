package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.ResetPasswordVerification;
import za.ac.cput.model.User;
import za.ac.cput.repository.implementation.PasswordResetRepositoryImp;
import za.ac.cput.service.EmailService;

import java.util.Date;
import java.util.UUID;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/17
 * @Time : 22:15
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImp {
    private final PasswordResetRepositoryImp passwordResetRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // For forgot password flow
    public void createPasswordResetTokenForUser(String email) {
        User user = passwordResetRepository.findUserByEmail(email);
        if (user == null) {
            throw new ApiException("User with the provided email does not exist");
        }
        String token = UUID.randomUUID().toString();
        Date expirationDate = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour expiration
        passwordResetRepository.saveResetToken(token, expirationDate, user.getId());
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) {
        ResetPasswordVerification verification = passwordResetRepository.findResetToken(token);
        if (verification == null || verification.getExpirationDate().before(new Date())) {
            throw new ApiException("Invalid or expired password reset token");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        passwordResetRepository.updateUserPassword(verification.getUserId(), encodedPassword);
    }

    // For change password flow
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = passwordResetRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ApiException("Current password is incorrect");
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        passwordResetRepository.updateUserPassword(userId, encodedPassword);
    }
}

