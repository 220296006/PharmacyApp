package za.ac.cput.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.model.Inventory;
import za.ac.cput.model.Response;
import za.ac.cput.service.implementation.InventoryServiceImp;
import java.util.Map;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryServiceImp inventoryService;

    @GetMapping("/list")
    public ResponseEntity<Response> getAll(){
        log.info("Get All Inventory");
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", this.inventoryService.getAll()))
                        .message("Inventory Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> save(@Validated @RequestBody Inventory inventory){
        log.info("Save Inventory: {}", inventory);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory",this.inventoryService.save(inventory)))
                        .message("Inventory Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> update(@Validated @RequestBody Inventory inventory){
        log.info("Update Inventory: {}", inventory);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", this.inventoryService.update(inventory)))
                        .message("Inventory Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete{inventoryId}")
    public ResponseEntity<Response> delete(@RequestBody Long inventoryId){
        log.info("Delete Inventory: {}", inventoryId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", this.inventoryService.delete(inventoryId)))
                        .message("Inventory Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
