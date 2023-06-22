package za.ac.cput;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.model.*;
import za.ac.cput.repository.*;

@RestController
@SpringBootApplication( exclude = {SecurityAutoConfiguration.class})
@Configuration
@ComponentScan("za.ac.cput.repository.implementation")
public class PharmacyApp {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyApp.class, args);
    }

     @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(InventoryRepository inventoryRepository, MedicationRepository medicationRepository,
                          PharmacyRepository pharmacyRepository, PrescriptionRepository prescriptionRepository, CustomerRepository customerRepository) {
        return args -> {
            inventoryRepository.save(new Inventory(null, "100", "100"));
            medicationRepository.save(new Medication(null, "Vicks", "Johnson&Johnson", "SP200"));
            pharmacyRepository.save(new Pharmacy(null, "Clicks", "CLKS2023", "INV101"));
            prescriptionRepository.save(new Prescription(null, "Cough Medicine", "Take 2 Spoons per day"));
            customerRepository.save(new Customer(null, new Name.Builder().setFirstName("John").setMiddleName("Doe").setLastName("Brown").build()));

        };
    }
}