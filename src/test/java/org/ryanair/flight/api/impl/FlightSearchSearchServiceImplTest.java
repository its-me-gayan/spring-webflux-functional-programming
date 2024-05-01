package org.ryanair.flight.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ryanair.flight.api.context.FlightSearchServiceTestContext;
import org.ryanair.flight.api.dto.*;
import org.ryanair.flight.api.model.Flight;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/2/24
 * Time: 5:01 PM
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FlightSearchService Unit Tests")
@Slf4j
class FlightSearchSearchServiceImplTest extends FlightSearchServiceTestContext {

    @Test
    void findFlights_OneDirectFightOnly_Success(){
        RequestDataDto mockRequestDto = createMockRequestDto();
        List<Flight> directFlightMockResponse = getDirectFlightMockResponse();

        Mockito.when(routeService.findAllPossibleRoutes(mockRequestDto.getDeparture() , mockRequestDto.getArrival()))
                        .thenReturn(Mono.just(getDirectRoute()));

        Mockito.when(scheduleService.getScheduledDirectFlightData(Mockito.any()))
                .thenReturn(Mono.just(Collections.singletonList(directFlightMockResponse.getFirst())));

        Mockito.when(serviceHelper.calculateNoOfMonthForTheProvidedDateRange(mockRequestDto))
                        .thenReturn(Collections.singletonList(getYearMonthData()));

        StepVerifier
                .create(flightSearchService.findFlights(mockRequestDto))
                .consumeNextWith(finalFlightResponseDtoList -> {
                    assertFalse(finalFlightResponseDtoList.isEmpty());
                    assertEquals(1, finalFlightResponseDtoList.size());
                    assertEquals(0, finalFlightResponseDtoList.getFirst().getStops());
                })
                .verifyComplete();
    }

    @Test
    void findFlights_WithOnlyInterconnectedNoDirect_Success() {
        List<Flight> intArrivingFlightMockResponse = getIntArrivingFlightMockResponse();
        List<Flight> intDepartingFlightMockResponse = getIntDepartingFlightMockResponse();
        RequestDataDto mockRequestDto = createMockRequestDto();

        Mockito.when(routeService.findAllPossibleRoutes(mockRequestDto.getDeparture() , mockRequestDto.getArrival()))
                .thenReturn(Mono.just(getInterConnectedRoute()));

    Mockito.when(scheduleService.getScheduledDepartingFlightData(Mockito.any()))
                .thenReturn(Mono.just(Collections.singletonList(intDepartingFlightMockResponse.getFirst())));

        Mockito.when(scheduleService.getScheduledArrivingFlightData(Mockito.any()))
                .thenReturn(Mono.just(Collections.singletonList(intArrivingFlightMockResponse.getLast())));


        StepVerifier
                .create(flightSearchService.findFlights(mockRequestDto))
                .consumeNextWith(finalFlightResponseDtoList -> {
                    assertFalse(finalFlightResponseDtoList.isEmpty());
                    assertEquals(1, finalFlightResponseDtoList.getFirst().getStops());
                    assertEquals(2, finalFlightResponseDtoList.getFirst().getLegs().size());
                })
                .verifyComplete();
    }

    @Test
    void findFlights_WithOneDirectAndOneInterconnected_Success() {
        List<Flight> intArrivingFlightMockResponse = getIntArrivingFlightMockResponse();
        List<Flight> intDepartingFlightMockResponse = getIntDepartingFlightMockResponse();
        List<Flight> directFlightMockResponse = getDirectFlightMockResponse();
        RequestDataDto mockRequestDto = createMockRequestDto();

        Mockito.when(routeService.findAllPossibleRoutes(mockRequestDto.getDeparture() , mockRequestDto.getArrival()))
                .thenReturn(Mono.just(getDirectAndInterConnectedRoutes()));

        Mockito.when(scheduleService.getScheduledDirectFlightData(Mockito.any()))
                .thenReturn(Mono.just(Collections.singletonList(directFlightMockResponse.getFirst())));

        Mockito.when(scheduleService.getScheduledDepartingFlightData(Mockito.any()))
                .thenReturn(Mono.just(Collections.singletonList(intDepartingFlightMockResponse.getFirst())));

        Mockito.when(scheduleService.getScheduledArrivingFlightData(Mockito.any()))
                .thenReturn(Mono.just(Collections.singletonList(intArrivingFlightMockResponse.getLast())));


        StepVerifier
                .create(flightSearchService.findFlights(mockRequestDto))
                .consumeNextWith(finalFlightResponseDtoList -> {
                    assertFalse(finalFlightResponseDtoList.isEmpty());
                    //checking direct flight
                    assertEquals(0, finalFlightResponseDtoList.getFirst().getStops());
                    //checking interconnected flight
                    assertEquals(1, finalFlightResponseDtoList.getLast().getStops());
                })
                .verifyComplete();
    }

    @Test
    void findFlights_WithMultipleDirectAndMultipleInterconnected_Success() {
        List<Flight> intArrivingFlightMockResponse = getIntArrivingFlightMockResponse();
        List<Flight> intDepartingFlightMockResponse = getIntDepartingFlightMockResponse();
        List<Flight> directFlightMockResponse = getDirectFlightMockResponse();
        RequestDataDto mockRequestDto = createMockRequestDto();

        Mockito.when(routeService.findAllPossibleRoutes(mockRequestDto.getDeparture() , mockRequestDto.getArrival()))
                .thenReturn(Mono.just(getDirectAndInterConnectedRoutes()));

        Mockito.when(scheduleService.getScheduledDirectFlightData(Mockito.any()))
                .thenReturn(Mono.just(directFlightMockResponse));

        Mockito.when(scheduleService.getScheduledDepartingFlightData(Mockito.any()))
                .thenReturn(Mono.just(intDepartingFlightMockResponse));

        Mockito.when(scheduleService.getScheduledArrivingFlightData(Mockito.any()))
                .thenReturn(Mono.just(intArrivingFlightMockResponse));


        StepVerifier
                .create(flightSearchService.findFlights(mockRequestDto))
                .consumeNextWith(finalFlightResponseDtoList -> {
                    assertFalse(finalFlightResponseDtoList.isEmpty());
                    List<FinalFlightResponseDto> directFlights = finalFlightResponseDtoList
                            .stream().filter(finalFlightResponseDto -> finalFlightResponseDto.getStops() == 0)
                            .toList();
                    List<FinalFlightResponseDto> interConFlights = finalFlightResponseDtoList
                            .stream().filter(finalFlightResponseDto -> finalFlightResponseDto.getStops() == 1)
                            .toList();
                    //checking direct flight section
                    assertTrue(directFlights.size() > 1);
                    //checking interconnected flight section
                    assertTrue(interConFlights.size() > 1);
                })
                .verifyComplete();

    }

}