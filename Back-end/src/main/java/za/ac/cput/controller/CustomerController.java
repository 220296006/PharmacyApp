package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.model.Customer;
import za.ac.cput.model.Response;
import za.ac.cput.service.CustomerService;

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
 * @Date : 2023/07/07
 * @Time : 19:06
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/count")
    public ResponseEntity<Integer> getCustomerCount() {
        log.info("Fetching Customer Count");
        Integer customerCount = customerService.getCustomerCount();
        return ResponseEntity.ok(customerCount);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createCustomer(@RequestBody @Validated Customer customer){
        log.info("Saving a Customer {}:", customer);
        customerService.createCustomer(customer);
        return ResponseEntity.created(getUri()).body(
                Response.builder()
                        .timeStamp( now())
                        .data(Map.of("customer", customer))
                        .message("Customer Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllCustomers(@RequestParam Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        log.info("Fetching customers for page {} of size {}:", page, pageSize);
          return ResponseEntity.ok().body(
                   Response.builder()
                    .timeStamp(now())
                    .data(Map.of("page", customerService.getAllCustomers(name.orElse(""), page.orElse(0), pageSize.orElse(10))))
                    .message("Customers retrieved")
                    .status(HttpStatus.OK)
                    .statusCode(OK.value())
                    .build()
           );
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Response> findCustomerById(@PathVariable Long id) {
        log.info("Fetching A Customer By Id: {}", id);
        return ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customer", customerService.findCustomerById(id)))
                        .message("Customer Fetched")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateCustomer(@Valid @RequestBody Customer customer){
           log.info("Updating Customer: {}", customer);
           customerService.updateCustomer(customer);
           return  ResponseEntity.created(getUri()).body(
                    Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customer", customer))
                        .message("Customer Updated")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
          );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCustomer(@PathVariable Long id){
        log.info("Deleting Customer: {}", id);
         return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("customer", customerService.deleteCustomer(id)))
                        .message("Customer Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
     private URI getUri(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/customer/get/<customerId>").toUriString());
    }
}
