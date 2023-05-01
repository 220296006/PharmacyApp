package za.ac.cput.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.model.Pharmacy;
import za.ac.cput.model.Response;
import za.ac.cput.service.implementation.PharmacyServiceImp;
import java.util.Map;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/pharmacy")
@RequiredArgsConstructor
@Slf4j
public class PharmacyController {

    private final PharmacyServiceImp pharmacyService;

    @GetMapping("/list")
    public ResponseEntity<Response> getAll(){
        log.info("Get All Pharmacy");
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", pharmacyService.getAll()))
                        .message("Pharmacy Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/save")
    public ResponseEntity<Response> save(@Validated @RequestBody Pharmacy pharmacy) {
        log.info("Save Pharmacy: {}", pharmacy);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", pharmacyService.save(pharmacy)))
                        .message("Pharmacy Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @GetMapping("/read")
    public ResponseEntity<Response> read(@Validated @RequestBody Long pharmacyId) {
        log.info("Read Pharmacy: {}", pharmacyId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", pharmacyService.read(pharmacyId)))
                        .message("Pharmacy Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PutMapping("/update/{pharmacyId}")
    public ResponseEntity<Response> update(@Validated @RequestBody Pharmacy pharmacy) {
        log.info("Update Pharmacy: {}", pharmacy);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", pharmacyService.save(pharmacy)))
                        .message("Pharmacy Updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete( @PathVariable("customerId") Long pharmacyId) {
        log.info("Delete Pharmacy: {}", pharmacyId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", pharmacyService.delete(pharmacyId)))
                        .message("Pharmacy Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
