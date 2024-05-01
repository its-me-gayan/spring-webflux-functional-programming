package org.ryanair.flight.api.service.frontend.impl;

import lombok.RequiredArgsConstructor;
import org.ryanair.flight.api.dto.PossibleRoutesDto;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.service.backend.BackendAPIService;
import org.ryanair.flight.api.service.frontend.RouteService;
import org.ryanair.flight.api.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 8:19 PM
 */

/**
 * Service implementation for finding all possible routes between departure and arrival airports.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteServiceImpl implements RouteService {
    private final BackendAPIService backendAPIService;

    /**
     * Finds all possible routes between the specified departure and arrival airports.
     *
     * @param departure The IATA code of the departure airport.
     * @param arrival   The IATA code of the arrival airport.
     * @return org.ryanair.flight.api.A Mono emitting a list of PossibleRoutesDto representing all possible routes between the given airports.
     */
    @Override
    public Mono<List<PossibleRoutesDto>> findAllPossibleRoutes(String departure , String arrival) {
        return backendAPIService
                .getRoutes(arrival, departure)
                .flatMap(routeAPIResponseModelList ->
                        extractAllDirectAndInterConnectedRoutesFromTheResponse(
                                routeAPIResponseModelList, departure, arrival)
                );

    }

    /**
     * Extracts all direct and interconnected routes from the provided list of route data.
     *
     * @param list             The list of route data.
     * @param departureIATACode The IATA code of the departure airport.
     * @param arrivalIATACode   The IATA code of the arrival airport.
     * @return org.ryanair.flight.api.A Mono emitting a list of PossibleRoutesDto representing all direct and interconnected routes.
     */
    private Mono<List<PossibleRoutesDto>> extractAllDirectAndInterConnectedRoutesFromTheResponse(List<RouteAPIResponseModel> list, String departureIATACode, String arrivalIATACode) {
        List<PossibleRoutesDto> possibleRoutesDtoList = new ArrayList<>();
        Optional<RouteAPIResponseModel> directRoute = findDirectRoute(list, departureIATACode, arrivalIATACode);
        if (directRoute.isPresent()) {
            PossibleRoutesDto possibleRoutesDto = new PossibleRoutesDto();
            possibleRoutesDto.setType(Constant.ROUTE_TYPE_DIRECT);
            possibleRoutesDto.setDirectRoute(directRoute.get());
            possibleRoutesDtoList.add(possibleRoutesDto);
        }

        findAndAddOneStopRoutes(list, departureIATACode, arrivalIATACode, possibleRoutesDtoList);
        return Mono.just(possibleRoutesDtoList);
    }

    /**
     * Finds direct routes between the specified departure and arrival airports.
     *
     * @param list             The list of route data.
     * @param departureIATACode The IATA code of the departure airport.
     * @param arrivalIATACode   The IATA code of the arrival airport.
     * @return org.ryanair.flight.api.A list of RouteAPIResponseModel representing all direct routes.
     */
    private Optional<RouteAPIResponseModel> findDirectRoute(List<RouteAPIResponseModel> list, String departureIATACode, String arrivalIATACode) {
        return list.stream()
                .filter(routeAPIResponseModel ->
                        routeAPIResponseModel.getAirportFrom().equals(departureIATACode) && routeAPIResponseModel.getAirportTo().equals(arrivalIATACode))
                .findAny();
    }

    /**
     * Finds and adds one-stop routes between the specified departure and arrival airports.
     *
     * @param list                 The list of route data.
     * @param departureIATACode     The IATA code of the departure airport.
     * @param arrivalIATACode       The IATA code of the arrival airport.
     * @param possibleRoutesDtoList The list to which the found routes will be added.
     */
    private void findAndAddOneStopRoutes(List<RouteAPIResponseModel> list, String departureIATACode, String arrivalIATACode, List<PossibleRoutesDto> possibleRoutesDtoList) {
        for (int i = 0; i < list.size(); i++) {
            RouteAPIResponseModel model = list.get(i);
            String airportTo = model.getAirportTo();
            if (departureIATACode.equals(model.getAirportFrom())) {
                for (RouteAPIResponseModel mm : list) {
                    if (airportTo.equals(mm.getAirportFrom()) && (mm.getAirportTo().equals(arrivalIATACode))) {
                            PossibleRoutesDto possibleRoutesDto = new PossibleRoutesDto();
                            possibleRoutesDto.setType(Constant.ROUTE_TYPE_INTER_CONNECTED);
                            possibleRoutesDto.setInterConnectedRoute(Arrays.asList(model, mm));
                            possibleRoutesDtoList.add(possibleRoutesDto);
                    }
                }
            }
        }
    }
}
