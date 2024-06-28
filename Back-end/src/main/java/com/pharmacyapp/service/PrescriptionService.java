package com.pharmacyapp.service;

import com.pharmacyapp.model.Prescription;

import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/11
 * @Time : 21:17
 **/
public interface PrescriptionService {
     void createPrescription( Prescription prescription);
     Collection<Prescription> getAllPrescriptions(String name, int page, int pageSize);
     Prescription findPrescriptionById(Long id);
     void updatePrescription( Prescription prescription);
     boolean deletePrescription(Long id);
     List<Prescription> getPrescriptionsByCustomerId(Long customerId);
     Integer getPrescriptionCount();

}
