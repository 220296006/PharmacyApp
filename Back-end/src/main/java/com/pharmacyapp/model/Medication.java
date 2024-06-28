package com.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:47
 **/
@Data
@SuperBuilder
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Medication {
    @Id
    private Long id;
    private String name;
    private String dosage;
    private String frequency;
    private Prescription prescription;

    @Getter
    private static final List<Medication> availableMedications = Arrays.asList(
            new Medication(1L, "Paracetamol", "10 mg", "Once a day", null ),
            new Medication(2L, "Amoxicillin", "10 mg", "Once a day", null),
            new Medication(3L, "Lisinopril", "15 mg", "Twice a day", null),
            new Medication(4L, "Omeprazole", "20 mg", "Once a day", null),
            new Medication(5L, "Disprin", "30 mg", "Three times a day", null),
            new Medication(6L, "Metformin", "5 mg", "Once a day", null),
            new Medication(7L, "Ibuprofen", "30 mg", "Twice a day", null),
            new Medication(8L, "Atorvastatin", "40 mg", "Once a day", null),
            new Medication(9L, "Amlodipine", "50 mg", "Three times a day", null),
            new Medication(10L, "GrandPa", "50 mg", "Three times a day", null)
    );

    public Medication(Long id, String name, String dosage, String frequency, Prescription prescription) {
    }
}
