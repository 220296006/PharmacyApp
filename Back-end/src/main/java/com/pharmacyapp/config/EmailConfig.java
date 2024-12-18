package com.pharmacyapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/06/27
 * @Time : 12:41
 **/
@Configuration
public class EmailConfig {

    @Value("${EMAIL_USER_NAME}")
    private String emailId;

    @Value("${EMAIL_PASSWORD}")
    private String emailPassword;

    @Value("${EMAIL_HOST_NAME}")
    private String emailHost;

    @Value("${EMAIL_PORT}")
    private int emailPort;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(emailId);
        mailSender.setPassword(emailPassword);
        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
