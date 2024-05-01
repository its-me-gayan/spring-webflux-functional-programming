package org.ryanair.flight.api.service.frontend;

import org.ryanair.flight.api.dto.ScheduledServiceDto;
import org.ryanair.flight.api.model.Flight;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 8:19 PM
 */

/**
 * This interface defines methods behaviors to retrieve scheduled flight data.
 * It provides functionality to get departing, arriving, and direct flight data.
 */
public interface ScheduleService {

    /**
     * Retrieves scheduled departing flight data based on the provided ScheduledServiceDto.
     *
     * @param scheduledServiceDto The DTO containing information for scheduling departing flights.
     * @return org.ryanair.flight.api.A Mono emitting FlightDataDto containing scheduled departing flight data.
     */
    Mono<List<Flight>>  getScheduledDepartingFlightData(ScheduledServiceDto scheduledServiceDto);

    /**
     * Retrieves scheduled arriving flight data based on the provided ScheduledServiceDto.
     *
     * @param scheduledServiceDto The DTO containing information for scheduling arriving flights.
     * @return org.ryanair.flight.api.A Mono emitting FlightDataDto containing scheduled arriving flight data.
     */
    Mono<List<Flight>> getScheduledArrivingFlightData(ScheduledServiceDto scheduledServiceDto);

    /**
     * Retrieves scheduled direct flight data based on the provided ScheduledServiceDto.
     *
     * @param scheduledServiceDto The DTO containing information for scheduling direct flights.
     * @return org.ryanair.flight.api.A Mono emitting FlightDataDto containing scheduled direct flight data.
     */
    Mono<List<Flight>> getScheduledDirectFlightData(ScheduledServiceDto scheduledServiceDto);
}
