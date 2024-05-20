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
import za.ac.cput.service.EmailService;

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

    @Autowired
    private final JavaMailSender emailSender;
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
            // Create context to hold variables to be passed to the Thymeleaf template
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("host", host);
            context.setVariable("token", token);
            String htmlContent = templateEngine.process("emailTemplate", context);

            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            log.info("Confirmation email sent to: {}", name);
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        String subject = "Password Reset Request";
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        String message = "To reset your password, click the link below:\n" + resetUrl;
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);
        emailSender.send(emailMessage);
    }
    }
