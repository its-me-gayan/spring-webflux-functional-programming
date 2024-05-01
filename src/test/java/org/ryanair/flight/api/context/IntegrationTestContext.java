package org.ryanair.flight.api.context;

import org.mockito.Mock;
import org.ryanair.flight.api.client.impl.RyanairAPIClient;
import org.ryanair.flight.api.helper.impl.ResponseGeneratorImpl;
import org.ryanair.flight.api.helper.impl.ServiceHelperImpl;
import org.ryanair.flight.api.service.backend.impl.RyanairBackendAPIServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.FlightSearchServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.RouteServiceImpl;
import org.ryanair.flight.api.service.frontend.impl.ScheduleServiceImpl;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 10:30 PM
 */


public class IntegrationTestContext {
    @Mock
    private FlightSearchServiceImpl flightSearchService;

    @Mock
    private ServiceHelperImpl serviceHelper;

    @Mock
    private RyanairBackendAPIServiceImpl backendAPIService;

    @Mock
    private RyanairAPIClient apiClient;

    @Mock
    private ResponseGeneratorImpl responseGenerator;

    @Mock
    private RouteServiceImpl routeService;
    @Mock
    private ScheduleServiceImpl scheduleService;

}
