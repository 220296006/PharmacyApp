package com.pharmacyapp.service;

import com.pharmacyapp.model.Confirmation;

import java.util.Collection;

public interface ConfirmationService {
    Confirmation findTokenByUserId(String userId);
    void saveConfirmation(Confirmation confirmation);
    Collection<Confirmation> list(int page, int pageSize);
    Confirmation findByConfirmationById(Long id);
    void updateConfirmation(Confirmation confirmation);
    boolean delete(Long id);
}
