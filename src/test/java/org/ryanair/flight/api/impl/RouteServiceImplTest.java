package org.ryanair.flight.api.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ryanair.flight.api.context.RouteServiceTestContext;
import org.ryanair.flight.api.dto.PossibleRoutesDto;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/2/24
 * Time: 5:02 PM
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("RouteService Unit Tests")
class RouteServiceImplTest extends RouteServiceTestContext {
    @Test
    void findPossibleRoutes_OperatorRyanairAndConnectingAirportNullOnly_Success() {
        Mockito.when(backendAPIService.getRoutes(Mockito.any(), Mockito.any())).thenReturn(Mono.just(getRouteResponse()));

        StepVerifier.create(routeService.findAllPossibleRoutes("AAL", "GRO"))
                .consumeNextWith(finalFlightResponseDtoList -> {

            filterDirectRouteFromResponse(finalFlightResponseDtoList).ifPresent(possibleRoutesDto -> {
                Assertions.assertEquals("RYANAIR", possibleRoutesDto.getDirectRoute().getOperator());
                Assertions.assertNull(possibleRoutesDto.getDirectRoute().getConnectingAirport());
            });

            List<PossibleRoutesDto> possibleRoutesDtos = filterInterConRouteFromResponse(finalFlightResponseDtoList);
            if (!possibleRoutesDtos.isEmpty()) {
                possibleRoutesDtos.forEach(possibleRoutesDto -> possibleRoutesDto.getInterConnectedRoute().forEach(routeAPIResponseModel -> {
                    Assertions.assertEquals("RYANAIR", routeAPIResponseModel.getOperator());
                    Assertions.assertNull(routeAPIResponseModel.getConnectingAirport());
                }));
            }

        }).verifyComplete();
    }
}