package org.ryanair.flight.api.service.frontend;

import org.ryanair.flight.api.dto.PossibleRoutesDto;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 8:19 PM
 */

/**
 * Service interface for finding all possible routes between departure and arrival airports.
 */
public interface RouteService {

    /**
     * Finds all possible routes between the specified departure and arrival airports.
     *
     * @param departure The IATA code of the departure airport.
     * @param arrival   The IATA code of the arrival airport.
     * @return org.ryanair.flight.api.A Mono emitting a list of PossibleRoutesDto representing all possible routes between the given airports.
     */
    Mono<List<PossibleRoutesDto>> findAllPossibleRoutes(String departure , String arrival);
}
