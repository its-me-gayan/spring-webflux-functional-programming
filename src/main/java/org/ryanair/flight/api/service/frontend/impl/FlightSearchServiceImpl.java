package org.ryanair.flight.api.service.frontend.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ryanair.flight.api.dto.*;
import org.ryanair.flight.api.exception.BackendInvocationException;
import org.ryanair.flight.api.helper.ServiceHelper;
import org.ryanair.flight.api.model.*;
import org.ryanair.flight.api.service.frontend.FlightSearchService;
import org.ryanair.flight.api.service.frontend.RouteService;
import org.ryanair.flight.api.service.frontend.ScheduleService;
import org.ryanair.flight.api.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.ryanair.flight.api.util.Constant.DATE_FORMAT_ISO;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/28/24
 * Time: 4:17 PM
 */

/**
 * Implementation of FlightSearchService that finds all available flights based on the given criteria.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FlightSearchServiceImpl implements FlightSearchService {

    private final ServiceHelper serviceHelper;
    private final RouteService routeService;
    private final ScheduleService scheduleService;

    /**
     * Finds all available flights based on the given request data.
     * @param requestDataDto The request data containing departure and arrival details.
     * @return org.ryanair.flight.api.A Mono emitting a list of FinalFlightResponseDto objects.
     */
    @Override
    public Mono<List<FinalFlightResponseDto>> findFlights(RequestDataDto requestDataDto) {
        String arrival = requestDataDto.getArrival();
        String departure = requestDataDto.getDeparture();
        log.debug("processing received request findFlights()");
        return routeService.findAllPossibleRoutes(departure, arrival) //finding all possible routes
                .flatMap(routeAPIResponseModels ->
                        processAllAvailableInterconnectedAndDirectFlights(routeAPIResponseModels, requestDataDto)) // finding all available flights
                .flatMap(availableFlightDto ->
                        processCollectedFlightsToFinalResponse(availableFlightDto, requestDataDto)); //combining all available flights together

    }

    /**
     * Processes collected flights to generate final flight response.
     * @param availableFlightDto The available flight data.
     * @param requestDataDto The request data.
     * @return org.ryanair.flight.api.A Mono emitting a list of FinalFlightResponseDto objects.
     */
    private Mono<List<FinalFlightResponseDto>> processCollectedFlightsToFinalResponse(AvailableFlightDto availableFlightDto, RequestDataDto requestDataDto) {
        log.debug("Started generating final response");
        List<FinalFlightResponseDto> finalFlightResponseDtoList = new ArrayList<>();

        //generating final response for the direct flights
        List<Flight> allAvailableDirectFlights = availableFlightDto.getDirectFlights();
        generateAndAttachDirectFlightsToFinaResponse(finalFlightResponseDtoList, allAvailableDirectFlights,requestDataDto);

        //generating final response for the interconnected flights with checking conditions
        List<InterConnectedFlightData> allFoundedInterconnectedFlights = availableFlightDto.getInterconnectedFlights()
                .stream().filter(interConnectedFlightData ->
                        StringUtils.hasText(interConnectedFlightData.getDepartSection()) &&
                                StringUtils.hasText(interConnectedFlightData.getArrivingSection())
                ).toList();
        generateAndAttachInterConnectedFlightsToFinaResponse(
                finalFlightResponseDtoList, allFoundedInterconnectedFlights, requestDataDto);

        log.debug("finished generating final response");

        return Mono.just(finalFlightResponseDtoList);
    }

    /**
     * Generates and attaches interconnected flights to the final flight response.
     * @param finalFlightResponseDtoList The list to which final flight response DTOs are added.
     * @param allFoundedInterconnectedFlights List of interconnected flight data.
     * @param requestDataDto The request data.
     */
    private void generateAndAttachInterConnectedFlightsToFinaResponse(List<FinalFlightResponseDto> finalFlightResponseDtoList, List<InterConnectedFlightData> allFoundedInterconnectedFlights, RequestDataDto requestDataDto) {
        if (!CollectionUtils.isEmpty(allFoundedInterconnectedFlights)) {
            HashMap<String, List<Flight>> arrivingFlightsDataMap = new HashMap<>();
            HashMap<String, List<Flight>> departingFlightsDataMap = new HashMap<>();

            serviceHelper.linearizingDepartingAndArrivingInterconnectedFlights(
                    departingFlightsDataMap, arrivingFlightsDataMap, allFoundedInterconnectedFlights);

            findAndMapRelatedInterConnectedFlights(
                    arrivingFlightsDataMap, departingFlightsDataMap, requestDataDto, finalFlightResponseDtoList);
        }
    }

    /**
     * Generates and attaches direct flights to the final flight response.
     * @param finalFlightResponseDtoList The list to which final flight response DTOs are added.
     * @param directFlights List of direct flight data.
     */
    private void generateAndAttachDirectFlightsToFinaResponse(List<FinalFlightResponseDto> finalFlightResponseDtoList, List<Flight> directFlights, RequestDataDto requestDataDto) {
        if (!CollectionUtils.isEmpty(directFlights)) {
            directFlights.forEach(flight -> finalFlightResponseDtoList.add(
                    FinalFlightResponseDto.builder()
                            .stops(0)
                            .legs(
                                    Collections.singletonList(
                                            DataLegs.builder()
                                                    .departureDateTime(flight.getDepartureTime())
                                                    .arrivalDateTime(flight.getArrivalTime())
                                                    .departureAirport(requestDataDto.getDeparture())
                                                    .arrivalAirport(requestDataDto.getArrival())
                                                    .build()))
                            .build()
            ));
        }
    }


    /**
     * Finds and maps related interconnected flights to the final flight response.
     * @param arrivingFlightsDataMap Map containing arriving flights data.
     * @param departingFlightsDataMap Map containing departing flights data.
     * @param requestDataDto The request data.
     * @param finalFlightResponseDtoList The list to which final flight response DTOs are added.
     */
    private void findAndMapRelatedInterConnectedFlights(HashMap<String, List<Flight>> arrivingFlightsDataMap, HashMap<String, List<Flight>> departingFlightsDataMap, RequestDataDto requestDataDto, List<FinalFlightResponseDto> finalFlightResponseDtoList) {
        departingFlightsDataMap.forEach((key, departingFlights) -> {
            String[] split = key.split("-");
            String splitArrivingAirport = split[1];
            departingFlights.forEach(departingFlight -> {
                List<Flight> arrivingFlights = arrivingFlightsDataMap.get(split[1] + "-" + requestDataDto.getArrival());
                arrivingFlights.forEach(arrivingFlight -> {

                    LocalDateTime flightArrivingDateTime = LocalDateTime
                            .parse(departingFlight.getArrivalTime(), DateTimeFormatter.ofPattern(DATE_FORMAT_ISO));

                    Flight closestFoundedArrivingFlight = serviceHelper
                            .findTwoHourAfterClosestFlightByGivenDateTime(flightArrivingDateTime, arrivingFlights);

                    if (Objects.nonNull(closestFoundedArrivingFlight)) {
                        DataLegs dataLegsDepart = DataLegs.builder()
                                .departureAirport(requestDataDto.getDeparture())
                                .arrivalAirport(splitArrivingAirport)
                                .arrivalDateTime(departingFlight.getArrivalTime())
                                .departureDateTime(departingFlight.getDepartureTime())
                                .build();
                        DataLegs dataLegsArrv = DataLegs.builder()
                                .departureAirport(splitArrivingAirport)
                                .arrivalAirport(requestDataDto.getArrival())
                                .arrivalDateTime(closestFoundedArrivingFlight.getArrivalTime())
                                .departureDateTime(closestFoundedArrivingFlight.getDepartureTime())
                                .build();

                       boolean areFlightsAlreadyChosen = finalFlightResponseDtoList
                                .stream()
                                .anyMatch(finalFlightResponseDto -> finalFlightResponseDto.getLegs().contains(dataLegsArrv) || finalFlightResponseDto.getLegs().contains(dataLegsDepart));

                       if(!areFlightsAlreadyChosen) {
                           List<DataLegs> legs = new ArrayList<>();
                           legs.add(dataLegsDepart);
                           legs.add(dataLegsArrv);
                           finalFlightResponseDtoList.add(FinalFlightResponseDto.builder().stops(1).legs(legs).build());
                       }
                    }
                });
            });
        });
    }


    /**
     * Process all available interconnected and direct flights based on the given possible routes and request data.
     * @param allPossibleRoute List of possible routes.
     * @param requestDataDto The request data.
     * @return org.ryanair.flight.api.A Mono emitting the available flight data.
     * @throws BackendInvocationException if an error occurs during backend invocation.
     */
    private Mono<AvailableFlightDto> processAllAvailableInterconnectedAndDirectFlights(List<PossibleRoutesDto> allPossibleRoute, RequestDataDto requestDataDto) throws BackendInvocationException {

        //filtering and get direct route from the allPossibleRoute list
        Optional<PossibleRoutesDto> directRouteOptional = allPossibleRoute
                .stream()
                .filter(possibleRoutesDto ->
                        possibleRoutesDto.getType().equals(Constant.ROUTE_TYPE_DIRECT))
                .findAny();

        Flux<InterConnectedFlightData> selectedInterconnectedFLightDataFlux = Flux.empty();
        Mono<List<Flight>> selectedDirectFlightListMono = Mono.empty();

        List<YearMonthDataDto> noOfMonthWithYear = serviceHelper.calculateNoOfMonthForTheProvidedDateRange(requestDataDto);

        log.debug("Processing direct routes - {}",Constant.ROUTE_TYPE_DIRECT);

        if (directRouteOptional.isPresent()) {
            RouteAPIResponseModel directRoute = directRouteOptional.get().getDirectRoute();
            log.debug("direct route detected and process - {} to {} " , directRoute.getAirportFrom() , directRoute.getAirportTo());
            selectedDirectFlightListMono = getAvailableFlightForTheDirectRouteMono(directRoute, noOfMonthWithYear, requestDataDto);
        } else {
            log.debug("No direct route founded");
        }

        //filtering and get interconnected routes from the allPossibleRoute list
        log.debug("Processing founded Interconnected routes - {}",Constant.ROUTE_TYPE_INTER_CONNECTED);

        List<PossibleRoutesDto> list = allPossibleRoute
                .stream()
                .filter(possibleRoutesDto ->
                        possibleRoutesDto.getType().equals(Constant.ROUTE_TYPE_INTER_CONNECTED))
                .toList();
        if (!list.isEmpty()) {
            log.debug("Interconnected routes detected and processing " );
            selectedInterconnectedFLightDataFlux = getInterConnectedAvailableFlightFlux(requestDataDto, list, noOfMonthWithYear);
        } else {
            log.debug("No interconnected routes found");
        }

        //combining direct flights and interconnected flights mono's together
        return selectedDirectFlightListMono.defaultIfEmpty(Collections.emptyList())
                .zipWith(selectedInterconnectedFLightDataFlux.collectList().defaultIfEmpty(Collections.emptyList()))
                .map(tuple -> {
            List<Flight> selectedDirectFlights = tuple.getT1();
            List<InterConnectedFlightData> interConnectedFlightData = tuple.getT2();
            // Create a new object to hold both types of data
            AvailableFlightDto combinedDetails = new AvailableFlightDto();
            combinedDetails.setDirectFlights(selectedDirectFlights);
            combinedDetails.setInterconnectedFlights(interConnectedFlightData);
            return combinedDetails;
        });

    }

    /**
     * Retrieves the flux of available interconnected flights.
     * @param requestDataDto The request data.
     * @param interConnectedRoutes Details of interconnected routes.
     * @param noOfMonthWithYear The list of YearMonthDataDto objects.
     * @return Flux emitting InterConnectedFlightData.
     */
    private Flux<InterConnectedFlightData> getInterConnectedAvailableFlightFlux(RequestDataDto requestDataDto, List<PossibleRoutesDto> interConnectedRoutes, List<YearMonthDataDto> noOfMonthWithYear) {

        return Flux.fromIterable(interConnectedRoutes)
                .flatMap(possibleRoutesDto -> Flux.fromIterable(noOfMonthWithYear)
                        .flatMap(yearMonthDataDto -> {
                            List<RouteAPIResponseModel> interConnectedRoute = possibleRoutesDto.getInterConnectedRoute();
                            ScheduledServiceDto scheduledServiceDto = ScheduledServiceDto.builder()
                                    .arrivingRouteData(interConnectedRoute.getLast()) // Arriving section
                                    .departingRouteData(interConnectedRoute.getFirst()) // Departing section
                                    .requestData(requestDataDto)
                                    .yearMonthData(yearMonthDataDto)
                                    .build();

                            Mono<List<Flight>> scheduledDepartingFlightData = scheduleService
                                    .getScheduledDepartingFlightData(scheduledServiceDto)
                                    .switchIfEmpty(Mono.just(Collections.emptyList()));

                            Mono<List<Flight>> scheduledArrivingFlightData = scheduleService
                                    .getScheduledArrivingFlightData(scheduledServiceDto)
                                    .switchIfEmpty(Mono.just(Collections.emptyList()));


                            return scheduledDepartingFlightData.zipWith(scheduledArrivingFlightData)
                                    .map(tuple -> {
                                        List<Flight> departingFlights = tuple.getT1();
                                        List<Flight> arrivingFlights = tuple.getT2();

                                        if (departingFlights.isEmpty() || arrivingFlights.isEmpty()) {
                                           return new InterConnectedFlightData();
                                        }
                                        return getInterConnectedFlightData(interConnectedRoute, departingFlights, arrivingFlights);

                                    });

                        })
                );
    }

    private static InterConnectedFlightData getInterConnectedFlightData(List<RouteAPIResponseModel> interConnectedRoute, List<Flight> departingFlights, List<Flight> arrivingFlights) {
        InterConnectedFlightData interConnectedFlightData = new InterConnectedFlightData();
        RouteAPIResponseModel first = interConnectedRoute.getFirst();
        RouteAPIResponseModel last = interConnectedRoute.getLast();
        interConnectedFlightData.setDepartSection(first.getAirportFrom() + "-" + first.getAirportTo());
        interConnectedFlightData.setArrivingSection(last.getAirportFrom() + "-" + last.getAirportTo());
        interConnectedFlightData.setDepartureFlightData(departingFlights);
        interConnectedFlightData.setArriveFlightData(arrivingFlights);
        return interConnectedFlightData;
    }


    /**
     * Retrieves the mono of available direct flights.
     * @param directRoute Details of the direct route.
     * @param noOfMonthWithYear The list of YearMonthDataDto objects.
     * @param requestDataDto The request data.
     * @return Mono emitting a list of FlightDataDto objects.
     */
    private Mono<List<Flight>> getAvailableFlightForTheDirectRouteMono(
            RouteAPIResponseModel directRoute,
            List<YearMonthDataDto> noOfMonthWithYear,
            RequestDataDto requestDataDto) {
        return Flux.fromIterable(noOfMonthWithYear)
                .flatMap(yearMonthDataDto -> {
                    ScheduledServiceDto build = ScheduledServiceDto.builder()
                            .directRouteData(directRoute) // direct route section
                            .requestData(requestDataDto)
                            .yearMonthData(yearMonthDataDto)
                            .build();
                    return scheduleService.getScheduledDirectFlightData(build);
                })
                .flatMapIterable(flightList -> flightList) // Flatten the nested lists
                .collectList();// Collect all flights into one list
    }
}
