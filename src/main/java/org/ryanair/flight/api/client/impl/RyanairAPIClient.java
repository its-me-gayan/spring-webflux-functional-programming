package org.ryanair.flight.api.client.impl;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ryanair.flight.api.client.APIClient;
import org.ryanair.flight.api.config.property.RyanairBackEndEndpointConfiguration;
import org.ryanair.flight.api.exception.BackendInvocationException;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIRequestModel;
import org.ryanair.flight.api.model.ScheduleAPIResponseModel;
import org.ryanair.flight.api.util.Constant;
import org.ryanair.flight.api.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of the APIClient interface to interact with the Ryanair backend services.
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RyanairAPIClient implements APIClient {


    private final WebClient webClient;
    private final RyanairBackEndEndpointConfiguration backEndEndpointConfiguration;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;



    /**
     * Retrieves a list of available routes from the Ryanair backend.
     *
     * @return org.ryanair.flight.api.A Mono emitting a list of RouteAPIResponseModel instances.
     * @throws BackendInvocationException if there's an error invoking the backend service.
     */
    @Override
    public Mono<List<RouteAPIResponseModel>> getRoutes() throws BackendInvocationException {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(Constant.DOWNSTREAM_SERVICE_NAME);
        return webClient.get()
                .uri(backEndEndpointConfiguration.getRouteEndpointURL())
                .retrieve()
                .onStatus(httpStatusCode ->
                        !httpStatusCode.is2xxSuccessful() , clientResponse ->
                        Mono.error(
                                new BackendInvocationException(ResponseMessage.ERR_SERVICE_UNAVAILABLE, ResponseMessage.ERR_INVALID_RESP_FROM_BACKEND+" - " +clientResponse.statusCode(), HttpStatus.resolve(clientResponse.statusCode().value())
                                )
                        )
                )
                .bodyToFlux(RouteAPIResponseModel.class)
                .transformDeferred(RetryOperator.of(retryRegistry.retry(Constant.DOWNSTREAM_SERVICE_NAME))) // ORDER - If above, retry will complete before a failure is recorded by the circuit breaker
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker)) //ORDER - If written below, circuit breaker will record a single failure after the max-retry
                .doOnError(CallNotPermittedException.class::isInstance, throwable -> {
                    log.error("Circuit Breaker is in [{}]... Providing fallback response without calling the API", circuitBreaker.getState());
                    throw new BackendInvocationException(ResponseMessage.ERR_SERVICE_UNAVAILABLE , throwable.getMessage() , HttpStatus.SERVICE_UNAVAILABLE);
                })
                .collectList();
    }


    /**
     * Retrieves flight schedules from the Ryanair backend based on the provided schedule request model.
     *
     * @param scheduleAPIRequestModel The schedule request model containing departure, arrival, year, and month information.
     * @return org.ryanair.flight.api.A Mono emitting a ScheduleAPIResponseModel instance.
     * @throws BackendInvocationException if there's an error invoking the backend service.
     */
    @Override
    public Mono<ScheduleAPIResponseModel> getSchedules(ScheduleAPIRequestModel scheduleAPIRequestModel) throws BackendInvocationException {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(Constant.DOWNSTREAM_SERVICE_NAME);
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(backEndEndpointConfiguration.getScheduleEndpointURL())
                                .build(
                                        scheduleAPIRequestModel.getDeparture(),
                                        scheduleAPIRequestModel.getArrival(),
                                        scheduleAPIRequestModel.getYear(),
                                        scheduleAPIRequestModel.getMonth()
                                )

                )
                .retrieve()
                .onStatus(httpStatusCode ->
                        !httpStatusCode.is2xxSuccessful() , clientResponse ->
                        Mono.error(
                                new BackendInvocationException(ResponseMessage.ERR_SERVICE_UNAVAILABLE, ResponseMessage.ERR_INVALID_RESP_FROM_BACKEND+" - " +clientResponse.statusCode(), HttpStatus.resolve(clientResponse.statusCode().value())
                                )
                        )
                )                .bodyToMono(ScheduleAPIResponseModel.class)
                .transformDeferred(RetryOperator.of(retryRegistry.retry(Constant.DOWNSTREAM_SERVICE_NAME))) // ORDER - If above, retry will complete before a failure is recorded by the circuit breaker
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker)) //ORDER - If written below, circuit breaker will record a single failure after the max-retry
                .doOnError(CallNotPermittedException.class::isInstance, throwable -> {
                    log.error("Circuit Breaker is in [{}]... Providing fallback response without calling the API", circuitBreaker.getState());
                    throw new BackendInvocationException(ResponseMessage.ERR_SERVICE_UNAVAILABLE , throwable.getMessage() , HttpStatus.SERVICE_UNAVAILABLE);
                });
    }

}
