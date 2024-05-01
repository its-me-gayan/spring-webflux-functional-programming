package org.ryanair.flight.api.client;

import org.ryanair.flight.api.exception.BackendInvocationException;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIRequestModel;
import org.ryanair.flight.api.model.ScheduleAPIResponseModel;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * APIClient interface defines behaviour methods for interacting with external APIs.
 */
public interface APIClient {

    /**
     * Retrieves a list of routes available from the backend API.
     * @return org.ryanair.flight.api.A Mono emitting a list of RouteAPIResponseModel objects.
     * @throws BackendInvocationException if there's an error invoking the backend API.
     */
     Mono<List<RouteAPIResponseModel>> getRoutes() throws BackendInvocationException;

    /**
     * Retrieves the schedules for flights based on the provided request parameters.
     * @param scheduleAPIRequestModel The request parameters specifying departure, arrival, year, and month.
     * @return org.ryanair.flight.api.A Mono emitting a ScheduleAPIResponseModel object representing the schedules.
     * @throws BackendInvocationException if there's an error invoking the backend API.
     */
     Mono<ScheduleAPIResponseModel> getSchedules(ScheduleAPIRequestModel scheduleAPIRequestModel) throws BackendInvocationException;

}
