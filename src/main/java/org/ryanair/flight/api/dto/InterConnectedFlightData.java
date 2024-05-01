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
 * Date: 3/29/24
 * Time: 11:49 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InterConnectedFlightData {
    private String departSection; //Exp : DUB-STN
    private String arrivingSection; //Exp : STN-WRO
    private List<Flight> departureFlightData;
    private List<Flight> arriveFlightData;
}
