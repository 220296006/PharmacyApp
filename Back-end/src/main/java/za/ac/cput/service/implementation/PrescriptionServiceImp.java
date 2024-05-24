package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Prescription;
import za.ac.cput.repository.PrescriptionRepository;
import za.ac.cput.service.PrescriptionService;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/11
 * @Time : 21:20
 **/

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImp implements PrescriptionService {
    private final PrescriptionRepository<Prescription> prescriptionRepository;

    @Override
    public void createPrescription(Prescription prescription) {

        prescriptionRepository.save(prescription);
    }

    @Override
    public Collection<Prescription> getAllPrescriptions(String name, int page, int pageSize) {
        return prescriptionRepository.list("customers", 1, 50);
    }

    @Override
    public Prescription findPrescriptionById(Long id) {
        return prescriptionRepository.read(id);
    }

    @Override
    public void updatePrescription(Prescription prescription) {
     prescriptionRepository.update(prescription);
    }

    @Override
    public boolean deletePrescription(Long id) {
        prescriptionRepository.delete(id);
        return true;
    }

    @Override
    public List<Prescription> getPrescriptionsByCustomerId(Long customerId) {
        return prescriptionRepository.findByCustomerId(customerId);
    }

    @Override
    public Integer getPrescriptionCount() {
        return prescriptionRepository.prescriptionCount();
    }
}
