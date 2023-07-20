package za.ac.cput.service;

import za.ac.cput.model.Medication;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 02:00
 **/
public interface MedicationService {
    void createMedication(Medication medication);
    Collection<Medication> getAllMedications(String name, int page, int pageSize);
    Medication findMedicationById(Long id);
    void updateMedication(Medication medication);
    boolean deleteMedication(Long id);
}
