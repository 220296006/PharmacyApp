package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public String getCurrentPassword(Long userId) {
        String password = passwordResetRepository.getCurrentPassword(userId);
        if (password == null) {
            throw new ApiException("Could not retrieve current password for user ID: " + userId);
        }
        return password;
    }

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
        passwordResetRepository.updateUserPassword(verification.getUser().getId(), encodedPassword);
    }

    // For change password flow
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) throws ApiException {
        try {
            String storedPasswordHash = passwordResetRepository.getCurrentPassword(userId);
            if (passwordEncoder.matches(currentPassword, storedPasswordHash)) {
                String newPasswordHash = passwordEncoder.encode(newPassword);
                passwordResetRepository.updateUserPassword(userId, newPasswordHash);
                log.info("Password changed for user ID: {}", userId);
            } else {
                throw new ApiException("Current password is incorrect.");
            }
        } catch (Exception e) {
            log.error("Error changing password for user ID: {}", userId, e);
            throw new ApiException("Error changing password: " + e.getMessage());
        }
    }
}