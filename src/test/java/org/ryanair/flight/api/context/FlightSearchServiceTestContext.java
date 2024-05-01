package org.ryanair.flight.api.context;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.ryanair.flight.api.dto.PossibleRoutesDto;
import org.ryanair.flight.api.dto.RequestDataDto;
import org.ryanair.flight.api.dto.YearMonthDataDto;
import org.ryanair.flight.api.helper.impl.ServiceHelperImpl;
import org.ryanair.flight.api.model.Flight;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.service.frontend.impl.FlightSearchServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.RouteServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.ScheduleServiceImpl;
import org.ryanair.flight.api.context.util.TestUtil;
import org.ryanair.flight.api.util.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/3/24
 * Time: 3:56 PM
 */
@Slf4j
public class FlightSearchServiceTestContext {
    @Mock
    public RouteServiceImpl routeService;

    @Spy
    public ServiceHelperImpl serviceHelper;

    @Mock
    public ScheduleServiceImpl scheduleService;

    @InjectMocks
    public FlightSearchServiceImpl flightSearchService;


    private static List<RouteAPIResponseModel> getRouteMockData = Collections.emptyList();
    private static List<Flight> directFlightMockResponse = Collections.emptyList();
    private static List<Flight> intDepartingFlightMockResponse = Collections.emptyList();
    private static List<Flight> intArrivingFlightMockResponse = Collections.emptyList();
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_ISO);

    @BeforeAll
    public static void init(){
        try {
            getRouteMockData = TestUtil.getRouteMockData();
            directFlightMockResponse = TestUtil.getDirectFlightMockResponse();
            intDepartingFlightMockResponse = TestUtil.getIntDepartingFlightMockResponse();
            intArrivingFlightMockResponse = TestUtil.getIntArrivingFlightMockResponse();
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("Test init failed - {}" ,ex.getMessage());

        }

    }

    public List<Flight> getDirectFlightMockResponse() {
        return directFlightMockResponse;
    }

    public List<Flight> getIntDepartingFlightMockResponse() {
        return intDepartingFlightMockResponse;
    }

    public List<Flight> getIntArrivingFlightMockResponse() {
        return intArrivingFlightMockResponse;
    }
    public RequestDataDto createMockRequestDto(){
        return RequestDataDto.builder()
                .departure("AAL")
                .arrival("GRO")
                .departureDateTime(LocalDateTime.parse("2024-04-02T07:00" , dateTimeFormatter))
                .arrivalDateTime(LocalDateTime.parse("2024-04-02T01:00" , dateTimeFormatter))
                .build();

    }
    public YearMonthDataDto getYearMonthData(){
        return new YearMonthDataDto(2024,4);
    }

    public List<PossibleRoutesDto> getDirectRoute(){
        PossibleRoutesDto direct = PossibleRoutesDto.builder()
                .type("DIRECT")
                .directRoute(getRouteMockData.getFirst())
                .build();
        return Collections.singletonList(direct);
    }

    public List<PossibleRoutesDto> getInterConnectedRoute(){
        PossibleRoutesDto inter_connected = PossibleRoutesDto.builder()
                .type("INTER_CONNECTED")
                .interConnectedRoute(Arrays.asList(getRouteMockData.get(1),getRouteMockData.get(2)))
                .build();
        return Collections.singletonList(inter_connected);
    }

    public List<PossibleRoutesDto> getDirectAndInterConnectedRoutes(){

        return Stream.concat(getDirectRoute().stream(), getInterConnectedRoute().stream()).toList();

    }
}
