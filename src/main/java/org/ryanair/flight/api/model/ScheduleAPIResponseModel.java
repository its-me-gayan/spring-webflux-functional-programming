package org.ryanair.flight.api.model;


import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleAPIResponseModel {

    private int month;
    private ArrayList<Day> days;

}
