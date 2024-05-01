package org.ryanair.flight.api.model;

import lombok.*;

import java.util.List;

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
public class Day {
    private int day;
    private List<Flight> flights;
}
