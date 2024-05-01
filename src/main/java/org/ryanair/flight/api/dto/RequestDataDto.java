package org.ryanair.flight.api.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/31/24
 * Time: 5:17 PM
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDataDto {
    private String arrival;
    private String departure;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
}
