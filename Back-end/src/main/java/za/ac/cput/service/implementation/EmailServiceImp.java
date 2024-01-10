package za.ac.cput.service.implementation;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Confirmation;
import za.ac.cput.service.EmailService;
import za.ac.cput.utils.EmailUtils;

import java.io.File;

import static za.ac.cput.utils.EmailUtils.getEmailMessage;

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
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailTemplate";
    public static final String TEXT_HTML_ENCODING = "text/html";
    private final JavaMailSender emailSender;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    @Async
     public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(EmailUtils.getEmailMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Failed to send email: " + exception.getMessage());
        }
    }

    @Async
    public void sendConfirmationEmail(Confirmation confirmation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(confirmation.getUser().getEmail());
        message.setSubject("Account Confirmation");
        message.setText("Dear " + confirmation.getUser().getFirstName() + ",\n\n"
                + "Thank you for registering an account with us. Please click on the link below to confirm your account:\n\n"
                + "Confirmation Link: https://example.com/confirm?token=" + confirmation.getToken() + "\n\n"
                + "If you did not register an account, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Your App Team");
        emailSender.send(message);
        log.info("Confirmation email sent to: {}", confirmation.getUser().getEmail());
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
//        try {
//            MimeMessage message = getMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
//            helper.setPriority(1);
//            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//            helper.setText(getEmailMessage(name, host, token));
//            //Add attachments
//            FileSystemResource shaun = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/shaun.jpg"));
//            FileSystemResource ID = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/ID.pdf"));
//            helper.addAttachment(Objects.requireNonNull(shaun.getFilename()), shaun);
//            helper.addAttachment(Objects.requireNonNull(ID.getFilename()), ID);
//            emailSender.send(message);
//        } catch (Exception exception) {
//            log.error(exception.getMessage());
//            log.info("Confirmation email sent to: {}", name);
//            throw new ApiException(exception.getMessage());
      //  }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        log.info("Sending Verification email from {} to {} with token {}:", name, to, token);
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));
            //Add attachments
            FileSystemResource shaun = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/shaun.jpg"));
            FileSystemResource ID = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/ID.pdf"));
            helper.addInline(getContentId(shaun.getFilename()), shaun);
            helper.addInline(getContentId(ID.getFilename()), ID);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
//        try {
//            Context context = new Context();
//            /*context.setVariable("name", name);
//            context.setVariable("url", getVerificationUrl(host, token));*/
//            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
//            String text = templateEngine.process(EMAIL_TEMPLATE, context);
//            MimeMessage message = getMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
//            helper.setPriority(1);
//            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
            //helper.setText(text, true);
            //Add attachments (Optional)
            /*FileSystemResource fort = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/fort.jpg"));
            FileSystemResource dog = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/dog.jpg"));
            FileSystemResource homework = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/homework.docx"));
            helper.addAttachment(fort.getFilename(), fort);
            helper.addAttachment(dog.getFilename(), dog);
            helper.addAttachment(homework.getFilename(), homework);*/
//            emailSender.send(message);
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new RuntimeException(exception.getMessage());
//        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
//        try {
//            MimeMessage message = getMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
//            helper.setPriority(1);
//            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
            //helper.setText(text, true);
//            Context context = new Context();
//            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
//            String text = templateEngine.process(EMAIL_TEMPLATE, context);
//
//            // Add HTML email body
//            MimeMultipart = new MimeMultipart("related");
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
//            mimeMultipart.addBodyPart(messageBodyPart);
//
//            // Add images to the email body
//            BodyPart imageBodyPart = new MimeBodyPart();
//            DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Downloads/images/dog.jpg");
//            imageBodyPart.setDataHandler(new DataHandler(dataSource));
//            imageBodyPart.setHeader("Content-ID", "image");
//            mimeMultipart.addBodyPart(imageBodyPart);
//            emailSender.send(message);
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new RuntimeException(exception.getMessage());
//        }

    }
      private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }
}
