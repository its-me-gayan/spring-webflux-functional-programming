package org.ryanair.flight.api.handler;

import lombok.RequiredArgsConstructor;
import org.ryanair.flight.api.dto.AbstractResponse;
import org.ryanair.flight.api.dto.RequestDataDto;
import org.ryanair.flight.api.helper.ResponseGenerator;
import org.ryanair.flight.api.service.frontend.FlightSearchService;
import org.ryanair.flight.api.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 2:36 PM
 */

/**
 * Responsible for handling requests related to flight search and providing appropriate responses.
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlightSearchHandler {

    private final FlightSearchService flightSearchService;
    private final ResponseGenerator responseGenerator;

    /**
     * Handles the request to find available flights based on the provided criteria.
     * @param request The incoming server request.
     * @return org.ryanair.flight.api.A Mono representing the server response.
     */
    public Mono<ServerResponse> findAvailableFlights(ServerRequest request) {
        return request.queryParam("departure")
                .flatMap(departure -> request.queryParam("arrival")
                        .flatMap(arrival -> request.queryParam("departureDateTime")
                                .flatMap(departureDateTime -> request.queryParam("arrivalDateTime")
                                        .map(arrivalDateTime -> buildRequestDataDto(departure, arrival, departureDateTime, arrivalDateTime))
                                )
                        )
                )
                .map(requestDataDto -> flightSearchService.findFlights(requestDataDto)
                        .flatMap(finalFlightResponseDtoList -> ServerResponse.ok().bodyValue(responseGenerator.processSuccessResponse(finalFlightResponseDtoList)))
                        .onErrorResume(throwable -> {
                            AbstractResponse abstractResponse = responseGenerator.processExceptionResponse(throwable);
                            return ServerResponse.status(abstractResponse.getResponseCode()).bodyValue(abstractResponse);
                        })
                )
                .orElseGet(() ->
                        ServerResponse.badRequest()
                                .bodyValue(
                                        responseGenerator
                                                .processErrorResponse(HttpStatus.BAD_REQUEST,
                                                        ResponseMessage.RESPONSE_MESSAGE_FAILED,
                                                        ResponseMessage.ERR_INVALID_REQ_PARAMETERS))
                                .onErrorResume(throwable -> {
                                    AbstractResponse abstractResponse = responseGenerator.processExceptionResponse(throwable);
                                    return ServerResponse.status(abstractResponse.getResponseCode()).bodyValue(abstractResponse);
                                })
                );
    }

    /**
     * Builds RequestDataDto object from the provided parameters.
     *
     * @param departure         Departure location.
     * @param arrival           Arrival location.
     * @param departureDateTime Departure date and time.
     * @param arrivalDateTime   Arrival date and time.
     * @return RequestDataDto object.
     */
    private RequestDataDto buildRequestDataDto(String departure, String arrival, String departureDateTime, String arrivalDateTime) {
        if (!validate(departure, arrival, departureDateTime, arrivalDateTime)) {
            return null;
        }
        return new RequestDataDto(arrival, departure, LocalDateTime.parse(departureDateTime), LocalDateTime.parse(arrivalDateTime));
    }

    /**
     * Validates request parameters.
     *
     * @param departure         Departure location.
     * @param arrival           Arrival location.
     * @param departureDateTime Departure date and time.
     * @param arrivalDateTime   Arrival date and time.
     * @return true if parameters are valid, otherwise false.
     */
    private boolean validate(String departure, String arrival, String departureDateTime, String arrivalDateTime) {
        boolean b = StringUtils.hasText(departure) && StringUtils.hasText(arrival) && StringUtils.hasText(departureDateTime) && StringUtils.hasText(arrivalDateTime);
        if(b){
            try {
                LocalDateTime.parse(departureDateTime);
                LocalDateTime.parse(arrivalDateTime);
            }catch (Exception ex){
                return false;
            }
        }
        return b;

    }
}