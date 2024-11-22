package com.cagritrk.dataservice.exeption;

/**
 * Custom exception to handle customer not found
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
