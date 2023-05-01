package za.ac.cput.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Medication;
import za.ac.cput.repository.MedicationRepository;
import za.ac.cput.service.MedicationService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MedicationServiceImp implements MedicationService {

    private final MedicationRepository medicationRepository;
    @Override
    public Medication save(Medication medication) {
        log.info("Saving Medication:{}", medication);
        return medicationRepository.save(medication);
    }

    @Override
    public Medication read(Long s) {
        log.info("Read Medication By ID:{}", s);
        return medicationRepository.findById(s).get();
    }

    @Override
    public Medication update(Medication medication) {
        log.info("Updating Medication:{}", medication);
        return medicationRepository.save(medication);
    }

    @Override
    public boolean delete(Long s) {
        log.info("Deleting Medication By Id:{}", s);
        medicationRepository.deleteById(s);
        return true;
    }

    @Override
    public List<Medication> getAll() {
        log.info("Get All Medication");
        return medicationRepository.findAll();
    }
}
