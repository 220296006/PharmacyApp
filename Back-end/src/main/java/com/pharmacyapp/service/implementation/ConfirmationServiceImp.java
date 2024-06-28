package com.pharmacyapp.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.pharmacyapp.model.Confirmation;
import com.pharmacyapp.repository.ConfirmationRepository;
import com.pharmacyapp.service.ConfirmationService;

import java.util.Collection;
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationServiceImp implements ConfirmationService {

    private final ConfirmationRepository<Confirmation> confirmationRepository;

    @Override
    public Confirmation findTokenByUserId(String userId) {
        log.info("Finding token by User ID: {}", userId);
        return confirmationRepository.findTokenByUserId(userId);
    }


    @Override
    public void saveConfirmation(Confirmation confirmation) {
    confirmationRepository.save(confirmation);
    }

    @Override
    public Collection<Confirmation> list(int page, int pageSize) {
        return confirmationRepository.list("confirmations", 1, 5 );
    }

    @Override
    public Confirmation findByConfirmationById(Long id) {
        return confirmationRepository.read(id);
    }

    @Override
    public void updateConfirmation(Confirmation confirmation) {
        confirmationRepository.update(confirmation);
    }

    @Override
    public boolean delete(Long id) {
    confirmationRepository.delete(id);
    return true;
    }
}
