package za.ac.cput.service;
import za.ac.cput.model.Prescription;

import java.util.List;

public interface PrescriptionService extends IService<Prescription, Long>{

    List<Prescription> getAll();
}
