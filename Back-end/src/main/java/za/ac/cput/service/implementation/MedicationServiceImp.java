package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Medication;
import za.ac.cput.repository.MedicationRepository;
import za.ac.cput.service.MedicationService;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 02:05
 **/
@Service
@RequiredArgsConstructor
public class MedicationServiceImp implements MedicationService {
    private final MedicationRepository<Medication> medicationRepository;
    @Override
    public void createMedication(Medication medication) {
     medicationRepository.save(medication);
    }

    @Override
    public Collection<Medication> getAllMedications(String name, int page, int pageSize) {
        return medicationRepository.list("medications", 1, 50);
    }

    @Override
    public Medication findMedicationById(Long id) {
        return medicationRepository.read(id);
    }

    @Override
    public void updateMedication(Medication medication) {
     medicationRepository.update(medication);
    }

    @Override
    public boolean deleteMedication(Long id) {
        medicationRepository.delete(id);
        return true;
    }

    @Override
    public List<Medication> getMedicationsByPrescriptionId(Long prescription_id) {
        return medicationRepository.findByPrescriptionId(prescription_id);
    }
}
