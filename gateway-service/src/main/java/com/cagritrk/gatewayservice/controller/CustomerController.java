package com.cagritrk.gatewayservice.controller;

import com.cagritrk.gatewayservice.adapter.DataServiceAdapter;
import com.cagritrk.gatewayservice.exception.CustomerNotFoundException;
import com.cagritrk.gatewayservice.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Endpoints for managing customer data")
public class CustomerController {

    private final DataServiceAdapter dataServiceAdapter;

    public CustomerController(DataServiceAdapter dataServiceAdapter) {
        this.dataServiceAdapter = dataServiceAdapter;
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Fetch all customer records currently stored in the database")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(dataServiceAdapter.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    @Operation(
            summary = "Get customer by ID",
            description = "Retrieve a specific customer by their unique ID",
            parameters = {
                    @Parameter(name = "customerId", description = "The unique ID of the customer", example = "DD37Cf93aecA6Dc")
            }
    )
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        try {
            Customer customer = dataServiceAdapter.getCustomerById(customerId);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException exp){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @Operation(
            summary = "Upload customer CSV",
            description = "Upload a CSV file containing customer data to the database"
    )
    public ResponseEntity<Void> uploadCustomerCsv(
            @RequestPart("file") @Parameter(description = "CSV file containing customer data", required = true)
            MultipartFile csvFile
    ) {
        dataServiceAdapter.uploadCustomerCsv(csvFile);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all customers", description = "Remove all customer records from the database")
    public ResponseEntity<Void> deleteAllCustomers() {
        dataServiceAdapter.deleteAllCustomers();
        return ResponseEntity.ok().build();
    }
}