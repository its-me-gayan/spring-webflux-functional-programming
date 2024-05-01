package org.ryanair.flight.api.dto;

import lombok.*;

import java.util.Objects;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 11:37 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataLegs {
    private String departureAirport;
    private String arrivalAirport;
    private String departureDateTime;
    private String arrivalDateTime;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DataLegs dataLegs = (DataLegs) obj;
        return Objects.equals(departureAirport, dataLegs.departureAirport) &&
                Objects.equals(arrivalAirport, dataLegs.arrivalAirport) &&
                Objects.equals(departureDateTime, dataLegs.departureDateTime) &&
                Objects.equals(arrivalDateTime, dataLegs.arrivalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);
    }
}
