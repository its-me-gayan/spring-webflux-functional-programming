package org.ryanair.flight.api.helper;

import org.ryanair.flight.api.dto.AbstractResponse;
import org.ryanair.flight.api.dto.FinalFlightResponseDto;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 6:45 PM
 */
public interface ResponseGenerator {

    /**
     * Processes a successful response based on the final flight response DTOs.
     *
     * @param finalResponse The list of final flight response DTOs.
     * @return AbstractResponse representing the processed success response.
     */
    AbstractResponse processSuccessResponse(List<FinalFlightResponseDto> finalResponse);

    /**
     * Processes an exception response based on the thrown throwable.
     *
     * @param throwable The thrown throwable.
     * @return AbstractResponse representing the processed exception response.
     */
    AbstractResponse processExceptionResponse(Throwable throwable);

    /**
     * Processes an error response with the provided HTTP status code, error message, and error message description.
     *
     * @param httpResponseCode    The HTTP status code of the error response.
     * @param errMessage          The error message.
     * @param errMessageDescription The description of the error message.
     * @return AbstractResponse representing the processed error response.
     */
    AbstractResponse processErrorResponse(HttpStatus httpResponseCode, String errMessage , String errMessageDescription);
}

