package za.ac.cput.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.model.Customer;
import za.ac.cput.model.Response;
import za.ac.cput.service.implementation.CustomerServiceImp;
import java.util.Map;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController  {

    private final CustomerServiceImp customerService;

    @GetMapping("/list")
    public ResponseEntity<Response> getAll() {
        log.info("Get all Customers");
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customers", customerService.list(1, 2)))
                        .message("Customers Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/save")
    public ResponseEntity<Response> save(@Validated @RequestBody Customer customer){
        log.info("Save customer: {}", customer);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customers", customerService.save(customer)))
                        .message("Customer Saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/read/{customerId}")
    public ResponseEntity<Response> read(@PathVariable Long customerId) {
        log.info("Read customer: {}", customerId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customers", customerService.read(customerId)))
                        .message("Customer Read")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
     public ResponseEntity<Response> update(@Validated @RequestBody Customer customer){
        log.info("Update customer: {}", customer);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customers", customerService.save(customer)))
                        .message("Customer Updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Response> delete(@PathVariable("customerId") Long customerId) {
        log.info("Delete customer: {}", customerId);
         return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("deleted", customerService.delete(customerId)))
                        .message("Customer Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
