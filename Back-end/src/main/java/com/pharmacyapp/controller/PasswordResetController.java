package com.pharmacyapp.controller;

import com.pharmacyapp.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.pharmacyapp.service.implementation.PasswordResetServiceImp;

import java.util.Map;

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
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        try {
            passwordResetService.createPasswordResetTokenForUser(email);
            return ResponseEntity.ok("Password reset email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
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