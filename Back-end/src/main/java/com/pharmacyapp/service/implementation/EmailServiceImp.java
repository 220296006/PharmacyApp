package com.pharmacyapp.service.implementation;

import com.pharmacyapp.exception.ApiException;
import com.pharmacyapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
@Configuration
public class EmailServiceImp implements EmailService {

    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "NEW USER VERIFICATION";

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final String host;
    private final String fromEmail;

    // Default constructor required for Spring proxying
    public EmailServiceImp() {
        this.emailSender = null;
        this.templateEngine = null;
        this.host = null;
        this.fromEmail = null;
    }

    @Autowired
    public EmailServiceImp(JavaMailSender emailSender, TemplateEngine templateEngine, Environment env) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.host = env.getProperty("VERIFY_EMAIL_HOST");
        this.fromEmail = env.getProperty("EMAIL_USER_NAME");
    }
    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            log.info("Attempting to send email to {}", name);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(EMAIL_TEMPLATE);
            helper.setFrom(fromEmail); // Set the 'from' email address here
            helper.setTo(to);

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("host", host);
            context.setVariable("token", token);
            String htmlContent = templateEngine.process("emailTemplate", context);

            helper.setText(htmlContent, true);
            emailSender.send(message);
            log.info("Successfully sent email to {}", name);
        } catch (Exception exception) {
            log.error("Failed to send email to {}: {}", name, exception.getMessage());
            throw new ApiException("Failed to send confirmation email to: " + name);
        }
    }

    @Override
    public void sendPasswordResetEmail(String name, String email, String token, String temporaryPassword) {
        try {
            log.info("Attempting to send password reset email to {}", email);
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
            log.info("Successfully sent password reset email to {}", email);
        } catch (Exception exception) {
            log.error("Failed to send password reset email to {}: {}", email, exception.getMessage());
            throw new ApiException("Failed to send password reset email to: " + email);
        }
    }
}
