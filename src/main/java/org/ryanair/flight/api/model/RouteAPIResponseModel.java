package org.ryanair.flight.api.model;


import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RouteAPIResponseModel {
    private String airportFrom;
    private String airportTo;
    private Object connectingAirport;
    private boolean newRoute;
    private boolean seasonalRoute;
    private String operator;
    private String carrierCode;
    private String group;
    private String[] similarArrivalAirportCodes;
    private String[] tags;
}
