package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.model.Prescription;
import za.ac.cput.model.Response;
import za.ac.cput.service.PrescriptionService;

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
 * @Date : 2023/07/11
 * @Time : 21:26
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/prescription")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @GetMapping("/count")
    public ResponseEntity<Integer> getCustomerCount() {
        log.info("Fetching Customer Count");
        Integer customerCount = prescriptionService.getPrescriptionCount();
        return ResponseEntity.ok(customerCount);
    }


    @PostMapping("/create")
    public ResponseEntity<Response> createPrescription(@RequestBody @Validated Prescription prescription){
        log.info("Saving a Prescription {}:", prescription);
        prescriptionService.createPrescription(prescription);
        return ResponseEntity.created(getUriPrescription()).body(
                Response.builder()
                        .timeStamp( now())
                        .data(Map.of("prescription", prescription))
                        .message("Prescription Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
    @GetMapping("/all")
    public ResponseEntity<Response> getAllPrescription(@RequestParam Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        log.info("Fetching prescriptions for page {} of size {}:", page, pageSize);
          return ResponseEntity.ok().body(
                   Response.builder()
                    .timeStamp(now())
                    .data(Map.of("page",prescriptionService.getAllPrescriptions(name.orElse(""), page.orElse(0), pageSize.orElse(10))))
                    .message("Prescriptions retrieved")
                    .status(HttpStatus.OK)
                    .statusCode(OK.value())
                    .build()
           );
    }
     @GetMapping("/read/{id}")
    public ResponseEntity<Response> findPrescriptionById(@PathVariable Long id){
        log.info("Fetching A Prescription By Id: {}", id);
        return  ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("prescription",  prescriptionService.findPrescriptionById(id)))
                        .message("Prescription Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/read/customer/{customerId}")
    public ResponseEntity<Response> findByCustomerId(@PathVariable Long customerId){
        log.info("Fetching A Prescription By Customer Id: {}", customerId);
        return  ResponseEntity.created(getUriCustomer()).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("prescription",  prescriptionService.getPrescriptionsByCustomerId(customerId)))
                        .message("Prescription Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
      @PutMapping("/update")
    public ResponseEntity<Response> updatePrescription(@Valid @RequestBody Prescription prescription){
           log.info("Updating Prescription: {}", prescription);
           prescriptionService.updatePrescription(prescription);
           return  ResponseEntity.created(getUriPrescription()).body(
                    Response.builder()
                        .timeStamp(now())
                        .data(Map.of("prescription", prescription))
                        .message("Prescription Updated")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
          );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deletePrescription(@PathVariable Long id){
        log.info("Deleting Prescription: {}", id);
         return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("prescription", prescriptionService.deletePrescription(id)))
                        .message("Prescription Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
     private URI getUriPrescription(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/prescription/get/<prescriptionId>").toUriString());
    }

    private URI getUriCustomer(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/prescription/get/<customerId>").toUriString());
    }

}
