package com.cagritrk.dataservice.repository;

import com.cagritrk.dataservice.models.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findByCustomerId(String customerId);
}
