package org.ryanair.flight.api.context;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.ryanair.flight.api.context.util.TestUtil;
import org.ryanair.flight.api.dto.RequestDataDto;
import org.ryanair.flight.api.dto.ScheduledServiceDto;
import org.ryanair.flight.api.dto.YearMonthDataDto;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIResponseModel;
import org.ryanair.flight.api.service.backend.impl.RyanairBackendAPIServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.ScheduleServiceImpl;
import org.ryanair.flight.api.util.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/3/24
 * Time: 4:35 PM
 */
public class ScheduleServiceTestContext {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_ISO);
    @Mock
    public RyanairBackendAPIServiceImpl backendAPIService;
    @InjectMocks
    public ScheduleServiceImpl scheduleService;

    public YearMonthDataDto getYearMonthData() {
        return new YearMonthDataDto(2024, 4);
    }

    private RouteAPIResponseModel getDirectRouteData() {
        try {
            return TestUtil.getRouteMockData().getFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RouteAPIResponseModel getDepartingRouteData() {
        try {
            return TestUtil.getRouteMockData().get(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RouteAPIResponseModel getArrivingRouteData() {
        try {
            return TestUtil.getRouteMockData().getLast();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RequestDataDto createMockRequestDto() {
        return RequestDataDto.builder().departure("STN").arrival("PMO").departureDateTime(LocalDateTime.parse("2024-04-01T07:00", dateTimeFormatter)).arrivalDateTime(LocalDateTime.parse("2024-04-30T01:00", dateTimeFormatter)).build();
    }

    public ScheduledServiceDto createMockScheduledServiceDto() {
        return ScheduledServiceDto.builder().directRouteData(getDirectRouteData()).departingRouteData(getDepartingRouteData()) // Departing section
                .arrivingRouteData(getArrivingRouteData()) // Arriving section
                .requestData(createMockRequestDto()).yearMonthData(getYearMonthData()).build();
    }

    public ScheduleAPIResponseModel getMockScheduleAPIResponseModel() {
        try {
            return TestUtil.getScheduledAPIMockResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
