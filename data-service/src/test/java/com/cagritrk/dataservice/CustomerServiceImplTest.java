package com.cagritrk.dataservice;

import com.cagritrk.dataservice.models.Customer;
import com.cagritrk.dataservice.repository.CustomerRepository;
import com.cagritrk.dataservice.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private final String sampleCustomerId = "DD37Cf93aecA6Dc";

    @Test
    void uploadCustomerCsvFile_shouldSaveParsedCustomers() throws IOException {
        String csvContent = """
            Index,Customer Id,First Name,Last Name,Company,City,Country,Phone 1,Phone 2,Email,Subscription Date,Website
            1,DD37Cf93aecA6Dc,Sheryl,Baxter,Rasmussen Group,East Leonard,Chile,229.077.5154,397.884.0519x718,zunigavanessa@smith.info,2020-08-24,http://www.stephenson.com/
            """;
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));

        customerService.uploadCustomerCsvFile(mockFile);

        verify(customerRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getCustomerById_shouldReturnCustomerIfExists() {
        Customer customer = createSampleCustomer();
        when(customerRepository.findByCustomerId(sampleCustomerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(sampleCustomerId);

        assertEquals(customer, result);
        verify(customerRepository).findByCustomerId(sampleCustomerId);
    }

    @Test
    void getAllCustomers_shouldReturnAllCustomers() {
        List<Customer> customers = List.of(createSampleCustomer());
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(customers, result);
        verify(customerRepository).findAll();
    }

    @Test
    void deleteAllCustomers_shouldCallRepositoryDeleteAll() {
        customerService.deleteAllCustomers();

        verify(customerRepository, times(1)).deleteAll();
    }

    private Customer createSampleCustomer() {
        return new Customer(
                null,
            sampleCustomerId,
            "Sheryl",
            "Baxter",
            "Rasmussen Group",
            "East Leonard",
            "Chile",
            "229.077.5154",
            "397.884.0519x718",
            "zunigavanessa@smith.info",
            LocalDate.of(2020, 8, 24),
            "http://www.stephenson.com/"
        );
    }
}