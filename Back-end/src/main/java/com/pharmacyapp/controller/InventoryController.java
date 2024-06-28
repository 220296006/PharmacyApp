package com.pharmacyapp.controller;

import com.pharmacyapp.model.Inventory;
import com.pharmacyapp.model.Medication;
import com.pharmacyapp.model.Response;
import com.pharmacyapp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 15:31
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/inventory")
public class InventoryController {
     private final InventoryService inventoryService;

    @PostMapping("/create")
    public ResponseEntity<Response> createInventory(@RequestBody @Validated Inventory inventory) {
        log.info("Creating an inventory item: {}", inventory);
        inventoryService.createInventory(inventory);
        return ResponseEntity.created(getUri()).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", inventory))
                        .message("Inventory item created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/medications")
    public ResponseEntity<List<Medication>> getAvailableMedications(Medication medication) {
        List<Medication> medications = inventoryService.getAvailableMedications(medication);
        return new ResponseEntity<>(medications, HttpStatus.OK);

    }
    @GetMapping("/all")
    public ResponseEntity<Response> getAllInventoryItems(@RequestParam Optional<String> name,
                                                         @RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<Integer> pageSize) {
        log.info("Fetching inventory items for page {} of size {}", page, pageSize);
        return ok().body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", inventoryService.findAllInventory(name.orElse(""), page.orElse(0), pageSize.orElse(10))))
                        .message("Inventory items retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @GetMapping("/read/{id}")
    public ResponseEntity<Response> findInventoryItemById(@PathVariable Long id) {
        log.info("Fetching an inventory item by ID: {}", id);
        Inventory inventoryItem = inventoryService.findByInventoryId(id);
        if (inventoryItem == null) {
            return ResponseEntity.notFound().build();
        }
        return ok().body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", inventoryItem))
                        .message("Inventory item retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PutMapping("/update")
    public ResponseEntity<Response> updateInventory(@Valid @RequestBody Inventory inventory) {
        log.info("Updating inventory item: {}", inventory);
        inventoryService.updateInventory(inventory);
        return ResponseEntity.created(getUri()).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("inventory", inventory))
                        .message("Inventory item updated")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteInventory(@PathVariable Long id) {
        log.info("Deleting inventory item with ID: {}", id);
        boolean deleted = inventoryService.deleteInventory(id);
        if (deleted) {
            return ok().body(
                    Response.builder()
                            .timeStamp(now())
                            .message("Inventory item deleted")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/inventory/read/{id}").toUriString());
    }
}
