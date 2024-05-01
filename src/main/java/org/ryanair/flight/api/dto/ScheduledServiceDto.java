package org.ryanair.flight.api.dto;

import lombok.*;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIRequestModel;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 8:44 PM
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ScheduledServiceDto {

    private ScheduleAPIRequestModel scheduleApiRequestData;
    private RouteAPIResponseModel departingRouteData;
    private RouteAPIResponseModel arrivingRouteData;
    private RouteAPIResponseModel directRouteData;
    private YearMonthDataDto yearMonthData;
    private RequestDataDto requestData;
}
