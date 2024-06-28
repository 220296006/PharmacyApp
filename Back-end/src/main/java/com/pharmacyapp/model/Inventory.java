package com.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:33
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Inventory {
    @Id
    private Long id;
    private String name;
    private String description;
    private BigInteger quantity;
    private BigInteger price;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Medication medication;


    public static final List<Inventory> inventoryItems = Arrays.asList(
            new Inventory(1L, "Paracetamol", "Headache Tablet", new BigInteger("100"), new BigInteger("5"), LocalDateTime.now(), null, null),
            new Inventory(2L, "Amoxicillin", "Antibiotic", new BigInteger("50"), new BigInteger("12"), LocalDateTime.now(), null, null),
            new Inventory(3L, "Lisinopril", "Blood pressure medication",new BigInteger("30"), new BigInteger("8"), LocalDateTime.now(), null, null),
            new Inventory(4L, "Omeprazole", "Acid reflux medication", new BigInteger("60"), new BigInteger("20"), LocalDateTime.now(), null, null),
            new Inventory(5L, "Disprin", "Pain medication", new BigInteger("60"), new BigInteger("20"), LocalDateTime.now(), null, null),
            new Inventory(6L, "Metformin", "Diabetes medication",new BigInteger("25"), new BigInteger("18"), LocalDateTime.now(), null, null),
            new Inventory(7L, "Ibuprofen", "Anti-inflammatory", new BigInteger("35"), new BigInteger("10"), LocalDateTime.now(), null, null),
            new Inventory(8L, "Atorvastatin", "Cholesterol medication",new BigInteger("45"), new BigInteger("25"), LocalDateTime.now(), null, null),
            new Inventory(9L, "Amlodipine", "Blood pressure medication",new BigInteger("55"), new BigInteger("14"), LocalDateTime.now(), null, null),
            new Inventory(10L, "GrandPa", "Pain medication",new BigInteger("55"), new BigInteger("14"), LocalDateTime.now(), null, null)
    );

}
