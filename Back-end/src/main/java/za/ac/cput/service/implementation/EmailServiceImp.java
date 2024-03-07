package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import za.ac.cput.exception.ApiException;
import za.ac.cput.service.EmailService;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:50
 **/

@Service
@RequiredArgsConstructor
@Slf4j
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


}
