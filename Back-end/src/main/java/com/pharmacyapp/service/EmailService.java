package com.pharmacyapp.service;

import org.springframework.stereotype.Service;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:47
 **/
@Service
public interface EmailService {
    void sendMimeMessageWithAttachments(String name, String to, String token);
    void sendPasswordResetEmail(String name,String email, String token, String temporaryPassword);
}
