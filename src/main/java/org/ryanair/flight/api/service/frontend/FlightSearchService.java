package org.ryanair.flight.api.service.frontend;

import org.ryanair.flight.api.dto.FinalFlightResponseDto;
import org.ryanair.flight.api.dto.RequestDataDto;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 4:17 PM
 */

/**
 * Service interface defining methods behaviours to find available flights.
 */
public interface FlightSearchService {

    /**
     * Finds all available flights based on the provided request data.
     *
     * @param requestDataDto The request data containing flight search parameters.
     * @return org.ryanair.flight.api.A Mono emitting a list of FinalFlightResponseDto objects representing available flights.
     */
    Mono<List<FinalFlightResponseDto>> findFlights(RequestDataDto requestDataDto);
}
