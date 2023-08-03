package za.ac.cput;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication()
@EnableAsync
public class PharmacyApp {

    public static void main(String[] args) {SpringApplication.run(PharmacyApp.class, args);
    }
private static final int STRENGTH = 12;

     @Bean
     public PasswordEncoder appPasswordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }

}