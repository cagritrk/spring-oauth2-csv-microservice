package com.cagritrk.dataservice.controller;

import com.cagritrk.dataservice.exeption.CustomerNotFoundException;
import com.cagritrk.dataservice.exeption.InvalidCsvFormatException;
import com.cagritrk.dataservice.models.Customer;
import com.cagritrk.dataservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST Controller for managing customer data.
 *
 * Provides endpoints to upload, retrieve, and delete customer records stored in the database.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Constructor to initialize CustomerService.
     *
     * @param customerService The service layer handling customer operations.
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves all customer records stored in the database.
     *
     * @return ResponseEntity containing the list of all customer records.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    /**
     * Retrieves a specific customer based on their unique customer ID.
     *
     * @param customerId The unique ID identifying the customer.
     * @return ResponseEntity containing the requested customer record.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        try {
            Customer customer = customerService.getCustomerById(customerId);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException exp){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Uploads a CSV file and stores its customer records into the database.
     *
     * @param file The CSV file to be uploaded.
     * @return ResponseEntity indicating the success or failure of the operation.
     * @throws IOException If an error occurs during file processing.
     */
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadCustomerCsv(@RequestParam("file") MultipartFile file) throws IOException, InvalidCsvFormatException {
        customerService.uploadCustomerCsvFile(file);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes all customer records from the database.
     *
     * @return ResponseEntity indicating the success of the delete operation.
     */
    @DeleteMapping
    public ResponseEntity<Object> deleteAllCustomers() {
        customerService.deleteAllCustomers();
        return ResponseEntity.ok().build();
    }
}