package org.ryanair.flight.api.helper.impl;

import org.ryanair.flight.api.dto.AbstractResponse;
import org.ryanair.flight.api.dto.FinalFlightResponseDto;
import org.ryanair.flight.api.exception.BackendInvocationException;
import org.ryanair.flight.api.exception.DataProcessingCommonServiceException;
import org.ryanair.flight.api.exception.DataValidationException;
import org.ryanair.flight.api.helper.ResponseGenerator;
import org.ryanair.flight.api.util.Constant;
import org.ryanair.flight.api.util.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 6:46 PM
 */
@Component
public class ResponseGeneratorImpl implements ResponseGenerator {
    /**
     * Processes the response based on the final flight response DTOs.
     *
     * @param finalResponse The final flight response DTOs.
     * @return AbstractResponse representing the processed response.
     */
    @Override
    public AbstractResponse processSuccessResponse(List<FinalFlightResponseDto> finalResponse){
        if(!finalResponse.isEmpty()){
            long directFlightCount = finalResponse.stream().filter(finalFlightResponseDto -> finalFlightResponseDto.getStops() == 0).count();
            long interConnectedFlightCount = finalResponse.stream().filter(finalFlightResponseDto -> finalFlightResponseDto.getStops() > 0).count();
            return createAbstractResponse(finalResponse, HttpStatus.OK , ResponseMessage.RESPONSE_MESSAGE_SUCCESS,ResponseMessage.RESPONSE_MESSAGE_SUCCESS + String.format(ResponseMessage.RESPONSE_DESCRIPTION_INFO , directFlightCount,interConnectedFlightCount));
        }else {
            return createAbstractResponse(finalResponse,HttpStatus.NO_CONTENT , ResponseMessage.RESPONSE_MESSAGE_NO_CONTENT,ResponseMessage.RESPONSE_MESSAGE_NO_CONTENT);
        }
    }
    /**
     * Processes the error response based on the thrown exception.
     *
     * @param throwable The thrown exception.
     * @return AbstractResponse representing the processed error response.
     */

    @Override
    public AbstractResponse processExceptionResponse(Throwable throwable){

        return switch (throwable) {
            case BackendInvocationException backendInvocationException ->
                    processErrorResponseInternal(backendInvocationException.getStatus(), backendInvocationException.getMessage(), backendInvocationException.getMessageDescription());
            case DataProcessingCommonServiceException dataProcessingCommonServiceException ->
                    processErrorResponseInternal(HttpStatus.BAD_REQUEST, ResponseMessage.RESPONSE_MESSAGE_FAILED, ResponseMessage.RESPONSE_MESSAGE_FAILED +" : "+throwable.getMessage());
            case DataValidationException dataValidationException ->
                    processErrorResponseInternal(HttpStatus.BAD_REQUEST, ResponseMessage.RESPONSE_MESSAGE_FAILED, ResponseMessage.RESPONSE_MESSAGE_FAILED +" : "+throwable.getMessage());
            case null, default -> {
                assert throwable != null;
                yield processErrorResponseInternal(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.RESPONSE_MESSAGE_FAILED, ResponseMessage.RESPONSE_MESSAGE_FAILED +" : "+throwable.getMessage());
            }
        };
    }

    /**
     * Creates an AbstractResponse for error scenarios with the provided HTTP status code, message, and message description.
     *
     * @param httpResponseCode The HTTP status code.
     * @param errMessage The message of the response.
     * @param errMessageDescription The description of the message.
     * @return AbstractResponse representing the created error response.
     */
    @Override
    public AbstractResponse processErrorResponse(HttpStatus httpResponseCode, String errMessage, String errMessageDescription) {
        return processErrorResponseInternal(httpResponseCode,errMessage,errMessageDescription);
    }

    private AbstractResponse processErrorResponseInternal(HttpStatus httpResponseCode, String errMessage, String errMessageDescription) {
        return AbstractResponse.builder()
                .responseCode(httpResponseCode.value())
                .timeStamp(DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_ISO).format(LocalDateTime.now()))
                .messageDescription(errMessageDescription)
                .message(errMessage)
                .build();
    }

    /**
     * Creates an AbstractResponse with the provided data, HTTP status code, message, and message description.
     *
     * @param data The data to be included in the response.
     * @param httpResponseCode The HTTP status code.
     * @param message The message of the response.
     * @param messageDescription The description of the message.
     * @return AbstractResponse representing the created response.
     */
    private AbstractResponse createAbstractResponse(Object data , HttpStatus httpResponseCode,String message ,String messageDescription){
        return AbstractResponse.builder()
                .data(data)
                .responseCode(httpResponseCode.value())
                .timeStamp(DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_ISO).format(LocalDateTime.now()))
                .messageDescription(messageDescription)
                .message(message)
                .build();
    }
}
