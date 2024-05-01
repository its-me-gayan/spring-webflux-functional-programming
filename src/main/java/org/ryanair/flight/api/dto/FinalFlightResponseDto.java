package org.ryanair.flight.api.dto;

import lombok.*;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 11:37 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinalFlightResponseDto {
    private int stops;
    private List<DataLegs> legs;
}
