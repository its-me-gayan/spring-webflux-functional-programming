package org.ryanair.flight.api.service.backend.impl;

import lombok.RequiredArgsConstructor;
import org.ryanair.flight.api.client.APIClient;
import org.ryanair.flight.api.exception.BackendInvocationException;
import org.ryanair.flight.api.exception.DataValidationException;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIRequestModel;
import org.ryanair.flight.api.model.ScheduleAPIResponseModel;
import org.ryanair.flight.api.service.backend.BackendAPIService;
import org.ryanair.flight.api.util.Constant;
import org.ryanair.flight.api.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Implementation of the BackendAPIService interface responsible for interacting with the backend API.
 * This is an intermediate service layer to decouple the main business logic holding
 * service layer from the backend invocation layer
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RyanairBackendAPIServiceImpl implements BackendAPIService {

    private final APIClient apiClient;

    /**
     * Retrieves the routes between the given arrival and departure IATA codes.
     *
     * @param arrivalIATACode   The IATA code of the arrival airport.
     * @param departureIATACode The IATA code of the departure airport.
     * @return org.ryanair.flight.api.A Mono emitting a list of valid RouteAPIResponseModel objects.
     * @throws BackendInvocationException if an error occurs during backend invocation.
     */
    @Override
    public Mono<List<RouteAPIResponseModel>> getRoutes(String arrivalIATACode , String departureIATACode) throws BackendInvocationException {
        return apiClient.getRoutes().flatMap(this::filterAndGetValidRoutes);
    }

    /**
     * Retrieves the flight schedules based on the provided ScheduleAPIRequestModel.
     *
     * @param scheduleAPIRequestModel The request model containing schedule parameters.
     * @return org.ryanair.flight.api.A Mono emitting a ScheduleAPIResponseModel object.
     * @throws BackendInvocationException if an error occurs during backend invocation.
     */
    @Override
    public Mono<ScheduleAPIResponseModel> getSchedules(ScheduleAPIRequestModel scheduleAPIRequestModel) throws BackendInvocationException {
        return apiClient.getSchedules(scheduleAPIRequestModel);
    }


    /**
     * Filters and retrieves valid routes from the list of RouteAPIResponseModel objects.
     *
     * @param routeList The list of RouteAPIResponseModel objects to filter.
     * @return org.ryanair.flight.api.A Mono emitting a list of valid RouteAPIResponseModel objects.
     */
    private Mono<List<RouteAPIResponseModel>> filterAndGetValidRoutes(List<RouteAPIResponseModel> routeList){
        List<RouteAPIResponseModel> list = routeList.stream()
                .filter(
                        this::checkRouteValidityAndFilter
                ).toList();
        if(list.isEmpty()){
            return Mono.error(new DataValidationException(ResponseMessage.ERR_MSG_NO_VALID_ROUTE_FOUND));
        }else {
            return Mono.just(list);
        }
    }

    /**
     * Checks the validity of a route and filters it based on certain conditions.
     *
     * @param routeAPIResponseModel The RouteAPIResponseModel object to check.
     * @return True if the route is valid, false otherwise.
     */
    private boolean checkRouteValidityAndFilter(RouteAPIResponseModel routeAPIResponseModel){
        if(
                        Objects.isNull(routeAPIResponseModel.getConnectingAirport()) &&
                        routeAPIResponseModel.getOperator().equals(Constant.PROVIDER)
        ){
                return Boolean.TRUE;

        }else {
            return Boolean.FALSE;
        }
    }
}
