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
public class RyanairBackEndPropertyConfiguration {

    @Value("${backend.ryanair.api.base-url}")
    private String baseUrl;
    @Value("${backend.ryanair.api.read-timeout}")
    private Integer connectTimeout;
    @Value("${backend.ryanair.api.connect-timeout}")
    private Integer readTimeout;
    @Value("${backend.ryanair.api.write-timeout}")
    private Integer writeTimeout;
    @Value("${backend.ryanair.api.max-in-memory-buffer-size-mb}")
    private Integer maxInMemBufferSizeMb;
}
