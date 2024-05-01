package org.ryanair.flight.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ryanair.flight.api.model.Flight;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/31/24
 * Time: 12:51 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableFlightDto {
    private List<Flight> directFlights;
    private List<InterConnectedFlightData> interconnectedFlights;
}
