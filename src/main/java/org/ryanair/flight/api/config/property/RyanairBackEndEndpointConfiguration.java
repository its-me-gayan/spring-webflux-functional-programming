package org.ryanair.flight.api.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 2:24 PM
 */


@Configuration
@RefreshScope
@Getter
@Setter
public class RyanairBackEndEndpointConfiguration {

    @Value("${backend.ryanair.endpoint.routes}")
    private String routeEndpointURL;
    @Value("${backend.ryanair.endpoint.schedules}")
    private String scheduleEndpointURL;
}
