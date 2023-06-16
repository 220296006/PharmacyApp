package za.ac.cput.service.implementation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Prescription;
import za.ac.cput.repository.PrescriptionRepository;
import za.ac.cput.service.PrescriptionService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PrescriptionServiceImp implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    @Override
    public Prescription save(Prescription prescription) {
        log.info("Saving Prescription: {}", prescription);
        return prescriptionRepository.save(prescription);
    }

    @Override
    public Collection<Prescription> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Prescription read(Long s) {
        log.info("Read Prescription By ID: {}", s);
        return prescriptionRepository.findById(s).get();
    }

    @Override
    public Prescription update(Prescription prescription) {
        log.info("Updating Prescription By ID: {}", prescription);
        return prescriptionRepository.save(prescription);
    }

    @Override
    public boolean delete(Long s) {
        log.info("Deleting Prescription By ID: {}", s);
        prescriptionRepository.deleteById(s);
        return true;
    }

    @Override
    public List<Prescription> getAll() {
        log.info("Get All Prescription");
        return prescriptionRepository.findAll();
    }
}
