package org.ryanair.flight.api.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ryanair.flight.api.context.ScheduleServiceTestContext;
import org.ryanair.flight.api.dto.PossibleRoutesDto;
import org.ryanair.flight.api.dto.RequestDataDto;
import org.ryanair.flight.api.dto.ScheduledServiceDto;
import org.ryanair.flight.api.dto.YearMonthDataDto;
import org.ryanair.flight.api.model.Flight;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIRequestModel;
import org.ryanair.flight.api.model.ScheduleAPIResponseModel;
import org.ryanair.flight.api.service.backend.impl.RyanairBackendAPIServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.ScheduleServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.ryanair.flight.api.util.Constant.DATE_FORMAT_ISO;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/2/24
 * Time: 5:02 PM
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleService Unit Tests")
class ScheduleServiceImplTest extends ScheduleServiceTestContext {

    @Test
    void getScheduledDepartingFlights_Success() {
        Mockito.when(backendAPIService.getSchedules(Mockito.any()))
                .thenReturn(Mono.just(getMockScheduleAPIResponseModel()));

        Mono<List<Flight>> scheduledDepartingFlightData = scheduleService
                .getScheduledDepartingFlightData(createMockScheduledServiceDto());
        StepVerifier
                .create(scheduledDepartingFlightData)
                .consumeNextWith(flights -> Assertions.assertFalse(flights.isEmpty())).verifyComplete();
    }

    @Test
    void getScheduledArrivingFlights_Success() {
        Mockito.when(backendAPIService.getSchedules(Mockito.any()))
                .thenReturn(Mono.just(getMockScheduleAPIResponseModel()));

        Mono<List<Flight>> scheduledDepartingFlightData = scheduleService
                .getScheduledArrivingFlightData(createMockScheduledServiceDto());
        StepVerifier
                .create(scheduledDepartingFlightData)
                .consumeNextWith(flights -> Assertions.assertFalse(flights.isEmpty())).verifyComplete();
    }

    @Test
    void getScheduledDirectFlight_Success()  {
        Mockito.when(backendAPIService.getSchedules(Mockito.any()))
                .thenReturn(Mono.just(getMockScheduleAPIResponseModel()));

        Mono<List<Flight>> scheduledDirectFlightData = scheduleService
                .getScheduledDirectFlightData(createMockScheduledServiceDto());
        StepVerifier
                .create(scheduledDirectFlightData)
                .consumeNextWith(flights -> Assertions.assertFalse(flights.isEmpty())).verifyComplete();
    }
}