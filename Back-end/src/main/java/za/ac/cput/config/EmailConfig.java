package za.ac.cput.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(" smtp.gmail.com");
        mailSender.setPort(587); // Set the port according to your SMTP server configuration
        mailSender.setUsername("thabisomatsaba96@gmail.com");
        mailSender.setPassword(" ysjqkbfnhvpnfzwo");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Set to true for debugging purposes

        return mailSender;
    }
}
