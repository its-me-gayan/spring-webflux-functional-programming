package org.ryanair.flight.api.model;

import lombok.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 3:54 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Flight {
    private String carrierCode;
    private String number;
    private String departureTime;
    private String arrivalTime;
}
