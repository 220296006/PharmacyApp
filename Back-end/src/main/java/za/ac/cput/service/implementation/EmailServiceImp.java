package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.User;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.EmailService;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:50
 **/

@Service
@RequiredArgsConstructor
@Slf4j
@Configuration
public class EmailServiceImp implements EmailService {

    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "NEW USER VERIFICATION";

    @Autowired
    private JavaMailSender emailSender;



    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(EMAIL_TEMPLATE);
            helper.setFrom(fromEmail);
            helper.setTo(to);

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("host", host);
            context.setVariable("token", token);
            String htmlContent = templateEngine.process("emailTemplate", context);

            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (Exception exception) {
            throw new ApiException("Failed to send confirmation email to: " + name);
        }
    }

    @Override
    public void sendPasswordResetEmail(String name, String email, String token, String temporaryPassword) {
        try {
            String subject = "Password Reset Request";
            String resetUrl = "http://localhost:8080/reset-password?token=" + token;

            // Prepare email content with Thymeleaf template
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("temporaryPassword", temporaryPassword);
            context.setVariable("host", host);
            context.setVariable("token", token);
            String htmlContent = templateEngine.process("forgotPasswordTemplate", context);

            // Send email
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error("Failed to send password reset email to {}: {}", email, exception.getMessage());
            throw new ApiException("Failed to send password reset email to: " + email);
        }
    }


}