package za.ac.cput.service;
import za.ac.cput.model.Medication;
import java.util.List;

public interface MedicationService extends IService<Medication, Long> {

    List<Medication> getAll();

}

