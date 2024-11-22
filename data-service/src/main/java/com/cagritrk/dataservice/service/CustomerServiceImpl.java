package com.cagritrk.dataservice.service;

import com.cagritrk.dataservice.exeption.CustomerNotFoundException;
import com.cagritrk.dataservice.exeption.InvalidCsvFormatException;
import com.cagritrk.dataservice.models.Customer;
import com.cagritrk.dataservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String EXPECTED_HEADER = "Index,Customer Id,First Name,Last Name,Company,City,Country,Phone 1,Phone 2,Email,Subscription Date,Website";
    private static final int EXPECTED_COLUMN_COUNT = 12;

    private final CustomerRepository customerRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void uploadCustomerCsvFile(MultipartFile csvFile) throws IOException, InvalidCsvFormatException {
        List<Customer> customers = parseCsvData(csvFile);
        customerRepository.saveAll(customers);
    }

    @Override
    public Customer getCustomerById(String customerId) throws CustomerNotFoundException{
        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

    private List<Customer> parseCsvData(MultipartFile file) throws IOException, InvalidCsvFormatException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            validateCsvHeader(reader.readLine());
            return reader.lines()
                    .map(this::parseCsvLine)
                    .collect(Collectors.toList());
        }
    }

    private Customer parseCsvLine(String line) {
        String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handle quoted commas

        if (columns.length < EXPECTED_COLUMN_COUNT) {
            throw new InvalidCsvFormatException("Invalid CSV structure: each row must have " + EXPECTED_COLUMN_COUNT + " columns.");
        }

        return new Customer(
                null,  // index
                cleanValue(columns[1]),   // customerId
                cleanValue(columns[2]),   // firstName
                cleanValue(columns[3]),   // lastName
                cleanValue(columns[4]),   // company
                cleanValue(columns[5]),   // city
                cleanValue(columns[6]),   // country
                cleanValue(columns[7]),   // phone1
                cleanValue(columns[8]),   // phone2
                cleanValue(columns[9]),   // email
                parseDate(columns[10]),   // subscriptionDate
                cleanValue(columns[11])   // website
        );
    }

    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(cleanValue(dateString), dateFormatter);
    }

    private String cleanValue(String value) {
        return value == null ? "" : value.replace("\"", "").trim();
    }

    private void validateCsvHeader(String headerLine) throws InvalidCsvFormatException{
        if (headerLine == null || !headerLine.equals(EXPECTED_HEADER)) {
            throw new InvalidCsvFormatException(
                    "Invalid CSV header. Expected: " + EXPECTED_HEADER + ", but got: " + headerLine);
        }
    }
}