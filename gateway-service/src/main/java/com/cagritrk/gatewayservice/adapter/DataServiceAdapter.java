package com.cagritrk.gatewayservice.adapter;

import com.cagritrk.gatewayservice.exception.CustomerNotFoundException;
import com.cagritrk.gatewayservice.model.Customer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Component
public class DataServiceAdapter {

    private final String DATA_SERVICE_BASE_URL = "http://localhost:8081/api/customers";
    private final String CLIENT_REGISTRATION_ID = "gateway-client";

    private final RestClient restClient;

    public DataServiceAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Fetch all customers from the data service.
     *
     * @return List of Customer.
     */
    public List<Customer> getAllCustomers() {
        return restClient.get()
                .uri(DATA_SERVICE_BASE_URL)
                .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * Fetch a specific customer by ID.
     *
     * @param customerId String customer ID
     * @return Customer for the given ID.
     */
    public Customer getCustomerById(String customerId) throws CustomerNotFoundException{
        return restClient.get()
                .uri("%s/{customerId}".formatted(DATA_SERVICE_BASE_URL), customerId)
                .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, (request, response) -> {
                    throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
                })
                .body(Customer.class);
    }

    /**
     * Upload customer data from CSV file.
     *
     * @param csvFile The csv file to upload.
     */
    public void uploadCustomerCsv(MultipartFile csvFile) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file", csvFile.getResource());

        restClient.post()
                .uri("%s/upload".formatted(DATA_SERVICE_BASE_URL))
                .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Deletes all customer records.
     */
    public void deleteAllCustomers() {
        restClient.delete()
                .uri(DATA_SERVICE_BASE_URL)
                .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
                .retrieve()
                .toBodilessEntity();
    }
}
