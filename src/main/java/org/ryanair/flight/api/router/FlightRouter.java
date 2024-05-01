package org.ryanair.flight.api.router;

import org.ryanair.flight.api.handler.FlightSearchHandler;
import org.ryanair.flight.api.util.Endpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 2:35 PM
 */


/**
 * Configures the router function for flight related endpoints.
 */
@Configuration
public class FlightRouter {

    /**
     * Defines the routing function for flight interconnection endpoints.
     * @param handler The handler responsible for processing flight search requests.
     * @return The router function mapping incoming requests to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> flightInterConnectRoutes(FlightSearchHandler handler) {
        return RouterFunctions.route().GET(Endpoint.END_POINT_INTERCONNECT, handler::findAvailableFlights).build();
    }
}
