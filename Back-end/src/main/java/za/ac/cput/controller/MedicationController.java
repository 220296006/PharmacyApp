package za.ac.cput.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.model.Medication;
import za.ac.cput.model.Response;
import za.ac.cput.service.implementation.MedicationServiceImp;
import java.util.Map;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/medication")
@RequiredArgsConstructor
@Slf4j
public class MedicationController {

    private final MedicationServiceImp medicationService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAll(){
        log.info("Get All Medication");
        return ResponseEntity.ok( Response.builder()
                .timeStamp(now())
                .data(Map.of("medication", this.medicationService.getAll()))
                .message("Medication Retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @PostMapping("/save")
    public ResponseEntity< Response> save(@Validated @RequestBody Medication medication){
        log.info("Save Medication:{}",medication);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", this.medicationService.save(medication)))
                        .message("Inventory Retrieved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/read/{medicationId}")
    public ResponseEntity<Response> read(@PathVariable Long medicationId){
        log.info("Read Medication:{}",medicationId);
        return ResponseEntity.ok( Response.builder()
                .timeStamp(now())
                .data(Map.of("inventory", this.medicationService.read(medicationId)))
                .message("Inventory Retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<Response> update(@Validated @RequestBody Medication medication){
        log.info("Update Medication:{}",medication);
        return ResponseEntity.ok( Response.builder()
                .timeStamp(now())
                .data(Map.of("inventory", this.medicationService.update(medication)))
                .message("Inventory Retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @DeleteMapping("/delete/{medicationId}")
    public ResponseEntity<Response> delete(@RequestBody Long medicationId){
        log.info("Delete Medication:{}",medicationId);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .data(Map.of("inventory",  this.medicationService.delete(medicationId)))
                .message("Inventory Deleted")
                .status(OK)
                .statusCode(OK.value())
                .build()
        );
    }
}
