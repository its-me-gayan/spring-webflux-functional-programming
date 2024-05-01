package org.ryanair.flight.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 6:22 PM
 */

@Setter
@Getter
@AllArgsConstructor
@ToString
public class YearMonthDataDto {
    private int year;
    private int month;
}
