package za.ac.cput.service.implementation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.model.Pharmacy;
import za.ac.cput.repository.PharmacyRepository;
import za.ac.cput.service.PharmacyService;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PharmacyServiceImp implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    @Override
    public Pharmacy save(Pharmacy pharmacy) {
        log.info("Saving Pharmacy:{}", pharmacy);
        return pharmacyRepository.save(pharmacy);
    }

    @Override
    public Pharmacy read(Long s) {
        log.info("Reading Pharmacy By ID:{}", s);
        return pharmacyRepository.findById(s).get();
    }

    @Override
    public Pharmacy update(Pharmacy pharmacy) {
        log.info("Updating Pharmacy:{}", pharmacy);
        return pharmacyRepository.save(pharmacy);
    }

    @Override
    public boolean delete(Long s) {
        log.info("Deleting Pharmacy By ID:{}", s);
        pharmacyRepository.deleteById(s);
        return true;
    }

    @Override
    public List<Pharmacy> getAll() {
        log.info("Get All Pharmacy");
        return pharmacyRepository.findAll();
    }
}
