package com.cagritrk.dataservice.exeption;

/**
 * Custom exception to handle invalid CSV file structures.
 */
public class InvalidCsvFormatException extends RuntimeException {

    public InvalidCsvFormatException(String message) {
        super(message);
    }
}