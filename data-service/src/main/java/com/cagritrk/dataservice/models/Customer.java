package com.cagritrk.dataservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

/**
 * Represents a customer record from the customers.csv file.
 *
 * @param index             Unique identifier for row index
 * @param customerId        Unique identifier for the customer
 * @param firstName         Customer's first name
 * @param lastName          Customer's last name
 * @param company           Company associated with the customer
 * @param city              City where customer is located
 * @param country           Country where customer is located
 * @param phone1            Primary phone number
 * @param phone2            Secondary phone number
 * @param email             Email address
 * @param subscriptionDate  Date when customer subscribed (yyyy-MM-dd format)
 * @param website           Customer's website URL
 */
@Table("CUSTOMERS")
public record Customer(
        @Id @Column("INDEX") Integer index,
        @Column("CUSTOMER_ID") String customerId,
        @Column("FIRST_NAME") String firstName,
        @Column("LAST_NAME") String lastName,
        @Column("COMPANY") String company,
        @Column("CITY") String city,
        @Column("COUNTRY") String country,
        @Column("PHONE1") String phone1,
        @Column("PHONE2") String phone2,
        @Column("EMAIL") String email,
        @Column("SUBSCRIPTION_DATE") LocalDate subscriptionDate,
        @Column("WEBSITE") String website
) {}
