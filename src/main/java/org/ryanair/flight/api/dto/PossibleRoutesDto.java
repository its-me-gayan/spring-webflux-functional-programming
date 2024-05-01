package org.ryanair.flight.api.dto;

import lombok.*;
import org.ryanair.flight.api.model.RouteAPIResponseModel;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/29/24
 * Time: 7:38 PM
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PossibleRoutesDto {

    private String type;
    private RouteAPIResponseModel directRoute;
    private List<RouteAPIResponseModel> interConnectedRoute;
}
