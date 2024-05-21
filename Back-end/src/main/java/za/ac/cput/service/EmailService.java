package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.model.Confirmation;

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
