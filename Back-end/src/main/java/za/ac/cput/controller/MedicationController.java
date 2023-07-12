package za.ac.cput.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.model.Medication;
import za.ac.cput.model.Response;
import za.ac.cput.service.MedicationService;

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

    @PostMapping("/create")
    public ResponseEntity<Response> createCustomer(@RequestBody @Validated Medication medication){
        log.info("Saving a Customer {}:", medication);
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
                        .message("Medication Fetched")
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
}
