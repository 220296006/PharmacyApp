package com.pharmacyapp.controller;

import com.pharmacyapp.model.Medication;
import com.pharmacyapp.model.Response;
import com.pharmacyapp.service.MedicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 02:10
 **/

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/medication")
public class MedicationController {
    private final MedicationService medicationService;

    @GetMapping("/count")
    public ResponseEntity<Integer> getCustomerCount() {
        log.info("Fetching Customer Count");
        Integer customerCount = medicationService.getMedicationCount();
        return ResponseEntity.ok(customerCount);
    }


    @PostMapping("/create")
    public ResponseEntity<Response> createCustomer(@RequestBody @Validated Medication medication){
        log.info("Saving a Medication {}:", medication);
        medicationService.createMedication(medication);
        return ResponseEntity.created(getUri()).body(
                Response.builder()
                        .timeStamp( now())
                        .data(Map.of("customer", medication))
                        .message("Customer Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

       @GetMapping("/all")
      public ResponseEntity<Response> getAllMedications(@RequestParam Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        log.info("Fetching medications for page {} of size {}:", page, pageSize);
        return ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp( now())
                        .data(Map.of("medication", medicationService.getAllMedications(name.orElse(""), page.orElse(0), pageSize.orElse(10))))
                        .message("Medications retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
      @GetMapping("/read/{id}")
    public ResponseEntity<Response> findMedicationById(@PathVariable  Long id){
        log.info("Fetching A Medication By Id: {}", id);
        return  ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("medication",  medicationService.findMedicationById(id)))
                        .message("Medication Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/read/prescription/{prescription_id}")
    public ResponseEntity<Response> findMedicationsByPrescriptionId(@PathVariable Long prescription_id) {
        log.info("Fetching Medications By Prescription Id: {}", prescription_id);
        return ResponseEntity.created(getUriMedications()).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("medications", medicationService.getMedicationsByPrescriptionId(prescription_id)))
                        .message("Medications Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateMedication(@Valid @RequestBody Medication medication){
           log.info("Updating Customer: {}", medication);
           medicationService.updateMedication(medication);
           return  ResponseEntity.created(getUri()).body(
                    Response.builder()
                        .timeStamp(now())
                        .data(Map.of("medication", medication))
                        .message("Medication Updated")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
          );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteMedication(@PathVariable Long id){
        log.info("Deleting Medication: {}", id);
         return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("medication", medicationService.deleteMedication(id)))
                        .message("Medication Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
private URI getUri(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/medication/get/<customerId>").toUriString());
    }

    private URI getUriMedications(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/medication/get/<prescriptionId>").toUriString());
    }
}
