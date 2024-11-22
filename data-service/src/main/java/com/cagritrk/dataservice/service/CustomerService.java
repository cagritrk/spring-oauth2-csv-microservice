package com.cagritrk.dataservice.service;

import com.cagritrk.dataservice.models.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    void uploadCustomerCsvFile(MultipartFile csvFile) throws IOException;

    Customer getCustomerById(String customerId);

    List<Customer> getAllCustomers();

    void deleteAllCustomers();
}