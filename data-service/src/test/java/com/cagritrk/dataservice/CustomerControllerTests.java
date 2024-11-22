package com.cagritrk.dataservice;

import com.cagritrk.dataservice.exeption.InvalidCsvFormatException;
import com.cagritrk.dataservice.models.Customer;
import com.cagritrk.dataservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CustomerControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CustomerService customerService;

    private final String sampleCustomerId = "DD37Cf93aecA6Dc";

    @Test
    @WithMockUser
    public void getAllCustomers_returnsEmptyList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    public void getCustomerById_returnsCustomer() throws Exception {
        Customer customer = createSampleCustomer();

        when(customerService.getCustomerById(sampleCustomerId)).thenReturn(customer);

        mvc.perform(get("/api/customers/{customerId}", sampleCustomerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(sampleCustomerId))
                .andExpect(jsonPath("$.firstName").value("Sheryl"))
                .andExpect(jsonPath("$.lastName").value("Baxter"));
    }

    @Test
    @WithMockUser
    public void uploadCustomerCsv_returnsOk() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        doNothing().when(customerService).uploadCustomerCsvFile(any(MultipartFile.class));

        mvc.perform(multipart("/api/customers/upload")
                        .file("file", mockFile.getBytes())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(customerService, times(1)).uploadCustomerCsvFile(any(MultipartFile.class));
    }

    @Test
    @WithMockUser
    public void deleteAllCustomers_returnsOk() throws Exception {
        doNothing().when(customerService).deleteAllCustomers();

        mvc.perform(delete("/api/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteAllCustomers();
    }

    @Test
    public void unauthorized_getAllCustomers_returnsUnauthorized() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    void uploadCustomerCsvFile_shouldThrowExceptionForInvalidHeader() throws IOException, InvalidCsvFormatException {
//        String invalidCsvContent = """
//        Index,Wrong Id,First Name,Last Name,Company,City,Country,Phone 1,Phone 2,Email,Subscription Date,Website
//        1,DD37Cf93aecA6Dc,Sheryl,Baxter,Rasmussen Group,East Leonard,Chile,229.077.5154,397.884.0519x718,zunigavanessa@smith.info,2020-08-24
//        """;
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(invalidCsvContent.getBytes()));
//
//        assertThrows(InvalidCsvFormatException.class, () -> {
//            customerService.uploadCustomerCsvFile(mockFile);
//        });
//    }

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