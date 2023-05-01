package za.ac.cput.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.model.Prescription;
import za.ac.cput.model.Response;
import za.ac.cput.service.implementation.PrescriptionServiceImp;
import java.util.Map;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
@Slf4j
public class PrescriptionController {

    private final PrescriptionServiceImp prescriptionService;

    @GetMapping("/list")
    public ResponseEntity<Response> getAll(){
        log.info("Get All Prescriptions");
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", this.prescriptionService.getAll()))
                        .message("Pharmacy Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> save(@Validated @RequestBody Prescription prescription) {
        log.info("Save Prescription: {}", prescription);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", this.prescriptionService.save(prescription)))
                        .message("Pharmacy Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/read/{prescriptionId}")
    public ResponseEntity<Response> read(@Validated @RequestBody Long prescriptionId) {
        log.info("Read Prescription By ID: {}", prescriptionId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", this.prescriptionService.read(prescriptionId)))
                        .message("Pharmacy Read")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update/{prescriptionId}")
    public ResponseEntity<Response> update(@Validated @RequestBody Prescription prescriptionId){
        log.info("Update Prescription: {}", prescriptionId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy", this.prescriptionService.update(prescriptionId)))
                        .message("Pharmacy Updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete/{prescriptionId}")
    public ResponseEntity<Response> delete(@PathVariable Long prescriptionId){
        log.info("Delete Prescription: {}", prescriptionId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("pharmacy",  this.prescriptionService.delete(prescriptionId)))
                        .message("Pharmacy Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
