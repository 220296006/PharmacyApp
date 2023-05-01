package za.ac.cput.service;
import za.ac.cput.model.Pharmacy;

import java.util.List;

public interface PharmacyService extends IService<Pharmacy, Long>{

    List<Pharmacy> getAll();
}
