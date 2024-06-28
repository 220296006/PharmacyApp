package com.pharmacyapp.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.service.EmailService;

import javax.mail.internet.MimeMessage;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:50
 **/

@Service
@RequiredArgsConstructor
@Slf4j
@ComponentScan
public class EmailServiceImp implements EmailService {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String EMAIL_TEMPLATE = "NEW USER VERIFICATION";
    @Autowired
    private final JavaMailSender emailSender;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final TemplateEngine templateEngine;

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
