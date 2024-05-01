package org.ryanair.flight.api.intergration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ryanair.flight.api.context.IntegrationTestContext;
import org.ryanair.flight.api.util.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/1/24
 * Time: 1:18 PM
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration Tests (Flights)")
class FlightSearchIntegrationTest extends IntegrationTestContext {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findAvailableFlights_Success_Status200() {
        String workingStatus200UriEndpoint = Endpoint.END_POINT_INTERCONNECT + "?departure=DUB&arrival=STN&departureDateTime=2024-06-20T07:00&arrivalDateTime=2024-06-27T09:05";
        webTestClient.get()
                .uri(workingStatus200UriEndpoint)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.responseCode").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("Data retrieved successfully")
                .jsonPath("$.data").isNotEmpty()
                .jsonPath("$.data").isArray();
    }

    @Test
    void findAvailableFlights_Invalid_Request_Parameters_Status400() {
        String failedStatus400UriEndpoint = Endpoint.END_POINT_INTERCONNECT + "?&arrival=STN&departureDateTime=2024-06-20T07:00&arrivalDateTime=2024-06-27T09:05";
        webTestClient.get()
                .uri(failedStatus400UriEndpoint)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Data retrieved Failed")
                .jsonPath("$.messageDescription").isEqualTo("Invalid request parameters");
    }

    @Test
    void findAvailableFlights_NoContentNoFlights_Status200With502() {
        String failedNoContentUriEndpoint = Endpoint.END_POINT_INTERCONNECT + "?departure=DUB&arrival=STN1&departureDateTime=2024-06-20T07:00&arrivalDateTime=2024-06-27T09:05";
        webTestClient.get()
                .uri(failedNoContentUriEndpoint)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.responseCode").isEqualTo(HttpStatus.NO_CONTENT.value())
                .jsonPath("$.message").isEqualTo("No any related flights Found for the the given criteria");
    }
}