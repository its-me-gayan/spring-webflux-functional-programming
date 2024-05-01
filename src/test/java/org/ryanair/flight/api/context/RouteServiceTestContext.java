package org.ryanair.flight.api.context;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.ryanair.flight.api.dto.PossibleRoutesDto;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.service.backend.impl.RyanairBackendAPIServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.RouteServiceImpl;
import org.ryanair.flight.api.context.util.TestUtil;

import java.util.List;
import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/3/24
 * Time: 4:19 PM
 */
public class RouteServiceTestContext {

    @Mock
    public RyanairBackendAPIServiceImpl backendAPIService;

    @InjectMocks
    public RouteServiceImpl routeService;

    public List<RouteAPIResponseModel> getRouteResponse() {
        try {
            return TestUtil.getRouteMockData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PossibleRoutesDto> filterDirectRouteFromResponse(List<PossibleRoutesDto> possibleRoutesDtos) {
        return possibleRoutesDtos.stream()
                .filter(possibleRoutesDto -> possibleRoutesDto.getType().equalsIgnoreCase("DIRECT")).findAny();

    }

    public List<PossibleRoutesDto> filterInterConRouteFromResponse(List<PossibleRoutesDto> possibleRoutesDtos) {
        return possibleRoutesDtos.stream()
                .filter(possibleRoutesDto -> possibleRoutesDto.getType().equalsIgnoreCase("INTER_CONNECTED")).toList();

    }

}
