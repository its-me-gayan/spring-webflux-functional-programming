package org.ryanair.flight.api.exception;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/31/24
 * Time: 9:04 PM
 */
public class DataValidationException extends RuntimeException{
    public DataValidationException(String message) {
        super(message);
    }
}
