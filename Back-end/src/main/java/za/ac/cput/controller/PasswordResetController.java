package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.exception.ApiException;
import za.ac.cput.service.implementation.PasswordResetServiceImp;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/17
 * @Time : 22:47
 **/

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/user/password-reset")
@ComponentScan
@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class PasswordResetController {
    private final PasswordResetServiceImp passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<String> createPasswordResetToken(@RequestParam String email) {
        try {
            passwordResetService.createPasswordResetTokenForUser(email);
            return ResponseEntity.ok("Password reset token has been sent to your email.");
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}