package org.ryanair.flight.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ryanair.flight.api.model.RouteAPIResponseModel;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/2/24
 * Time: 6:10 PM
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RouteDetailsDto {
    private RouteAPIResponseModel directRoute;
    private List<RouteAPIResponseModel> interConnectedRoute;
}
