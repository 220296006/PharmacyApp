package za.ac.cput.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.model.Invoice;
import za.ac.cput.model.Response;
import za.ac.cput.service.InvoiceService;

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
 * @Time : 18:08
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping("/create")
    public ResponseEntity<Response> createInvoice(@RequestBody @Validated Invoice invoice){
        log.info("Creating an Invoice {}:", invoice);
        invoiceService.createInvoice(invoice);
        return ResponseEntity.created(getUri()).body(
                Response.builder()
                        .timeStamp( now())
                        .data(Map.of("invoice", invoice))
                        .message("Invoice Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllInvoices(@RequestParam Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
    log.info("Fetching invoices for page {} of size {}:", page, pageSize);
    return ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp( now())
                        .data(Map.of("invoices", invoiceService.findAllInvoices(name.orElse(""), page.orElse(0), pageSize.orElse(10))))
                        .message("Invoices retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
               );
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Response> findInvoiceById(@PathVariable  Long id) {
        log.info("Fetching an Invoice By Id: {}", id);
        return  ResponseEntity.ok().body(
                 Response.builder()
                        .timeStamp(now())
                        .data(Map.of("invoice",  invoiceService.findByInvoiceId(id)))
                        .message("Invoice Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
   }

   @PutMapping("/update")
   public ResponseEntity<Response> updateInvoice(@Valid @RequestBody Invoice invoice){
          log.info("Updating Invoice {}:" ,invoice);
          invoiceService.updateInvoice(invoice);
          return ResponseEntity.created(getUri()).body(
                 Response.builder()
                    .timeStamp( now())
                    .data(Map.of("invoice", invoice))
                    .message("Invoice Updated")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .build()
        );
   }

   @DeleteMapping("/delete/{id}")
  public ResponseEntity<Response> deleteInvoice(@PathVariable Long id) {
  log.info("Deleting Invoice: {}", id);
  return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("invoice", invoiceService.deleteInvoice(id)))
                        .message("Invoice Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
   }

   private URI getUri(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/invoice/get/<invoiceId>").toUriString());
    }
}
