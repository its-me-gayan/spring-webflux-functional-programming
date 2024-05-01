package org.ryanair.flight.api.helper;

import org.ryanair.flight.api.dto.InterConnectedFlightData;
import org.ryanair.flight.api.dto.RequestDataDto;
import org.ryanair.flight.api.dto.YearMonthDataDto;
import org.ryanair.flight.api.model.Flight;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 12:43 PM
 */


/**
 * Helper interface providing methods behaviors for various flight-related operations in the service layer.
 */
public interface ServiceHelper {

    /**
     * Finds the flight occurring two hours after the closest flight to the given date and time.
     * @param dateTime The reference date and time.
     * @param flightList The list of flights to search within.
     * @return The flight occurring two hours after the closest flight, or null if none is found.
     */
    Flight findTwoHourAfterClosestFlightByGivenDateTime(LocalDateTime dateTime, List<Flight> flightList);

    /**
     * Calculates the number of months for the provided date range.
     * @param requestDataDto The request data specifying the date range.
     * @return org.ryanair.flight.api.A list of YearMonthDataDto objects representing the months within the date range.
     */
    List<YearMonthDataDto> calculateNoOfMonthForTheProvidedDateRange(RequestDataDto requestDataDto);

    /**
     * Linearizes and organizes the data of interconnected flights into maps.
     * @param departFlightDataMap The map to store departing flight data.
     * @param arriveFlightDataMap The map to store arriving flight data.
     * @param interConnectedFlightData The list of interconnected flight data.
     */
    void linearizingDepartingAndArrivingInterconnectedFlights(HashMap<String, List<Flight>> departFlightDataMap, HashMap<String, List<Flight>> arriveFlightDataMap, List<InterConnectedFlightData> interConnectedFlightData);

}

