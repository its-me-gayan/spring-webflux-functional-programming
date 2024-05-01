package org.ryanair.flight.api.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScheduleAPIRequestModel {
    private String departure;
    private String arrival;
    private int year;
    private int month;
}
