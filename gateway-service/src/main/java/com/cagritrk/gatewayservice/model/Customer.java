package com.cagritrk.gatewayservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Represents a customer record from the customers.csv file.
 */
@Schema(description = "Represents a customer record with personal and contact information")
public record Customer(
        @Schema(description = "Unique identifier for the customer record", example = "1")
        Integer index,

        @Schema(description = "Unique customer identifier", example = "DD37Cf93aecA6Dc")
        String customerId,

        @Schema(description = "Customer's first name", example = "Sheryl")
        String firstName,

        @Schema(description = "Customer's last name", example = "Baxter")
        String lastName,

        @Schema(description = "Company associated with the customer", example = "Rasmussen Group")
        String company,

        @Schema(description = "City where customer is located", example = "East Leonard")
        String city,

        @Schema(description = "Country where customer is located", example = "Chile")
        String country,

        @Schema(description = "Primary phone number", example = "229.077.5154")
        String phone1,

        @Schema(description = "Secondary phone number", example = "397.884.0519x718", nullable = true)
        String phone2,

        @Schema(description = "Email address", example = "zunigavanessa@smith.info")
        String email,

        @Schema(description = "Date when customer subscribed (yyyy-MM-dd format)", example = "2020-08-24")
        LocalDate subscriptionDate,

        @Schema(description = "Customer's website URL", example = "https://www.example.com/", nullable = true)
        String website
) {}