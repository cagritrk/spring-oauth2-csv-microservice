package com.cagritrk.gatewayservice;

import com.cagritrk.gatewayservice.adapter.DataServiceAdapter;
import com.cagritrk.gatewayservice.controller.CustomerController;
import com.cagritrk.gatewayservice.exception.CustomerNotFoundException;
import com.cagritrk.gatewayservice.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DataServiceAdapter dataServiceAdapter;

    private final UUID testUuid = UUID.randomUUID();
    private final LocalDate testDate = LocalDate.of(2020, 8, 24);

    @Test
    @WithMockUser
    void getAllCustomers_shouldReturnCustomers() throws Exception {
        List<Customer> mockCustomers = List.of(
                new Customer(testUuid, "CUST001", "John", "Doe", "Acme Inc",
                        "New York", "USA", "1234567890", null,
                        "john@acme.com", testDate, "http://acme.com"),
                new Customer(testUuid, "CUST002", "Jane", "Smith", "Globex Corp",
                        "London", "UK", "9876543210", "5551234567",
                        "jane@globex.com", testDate.minusDays(10), null)
        );

        when(dataServiceAdapter.getAllCustomers()).thenReturn(mockCustomers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerId").value("CUST001"))
                .andExpect(jsonPath("$[1].email").value("jane@globex.com"));

        verify(dataServiceAdapter, times(1)).getAllCustomers();
    }

    @Test
    @WithMockUser
    void getCustomerById_shouldReturnCustomer() throws Exception {
        Customer mockCustomer = new Customer(testUuid, "CUST001", "John", "Doe",
                "Acme Inc", "New York", "USA",
                "1234567890", null, "john@acme.com",
                testDate, "http://acme.com");

        when(dataServiceAdapter.getCustomerById("CUST001")).thenReturn(mockCustomer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/CUST001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.subscriptionDate").value("2020-08-24"));

        verify(dataServiceAdapter, times(1)).getCustomerById("CUST001");
    }

    @Test
    @WithMockUser
    void uploadCustomerCsv_shouldReturnOk() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "customers.csv",
                "text/csv",
                "Index,Customer Id,First Name,Last Name,...".getBytes()
        );

        doNothing().when(dataServiceAdapter).uploadCustomerCsv(Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/customers/upload")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(dataServiceAdapter).uploadCustomerCsv(Mockito.any());
    }

    @Test
    @WithMockUser
    void deleteAllCustomers_shouldReturnOk() throws Exception {
        doNothing().when(dataServiceAdapter).deleteAllCustomers();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(dataServiceAdapter, times(1)).deleteAllCustomers();
    }

    @Test
    @WithMockUser
    void getCustomerById_notFound_shouldReturn404() throws Exception {
        when(dataServiceAdapter.getCustomerById("UNKNOWN")).thenThrow(
                new CustomerNotFoundException("Customer not found with id: UNKNOWN"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/UNKNOWN")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found with id: UNKNOWN"));
    }
}
