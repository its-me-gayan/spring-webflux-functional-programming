package org.ryanair.flight.api.context.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ryanair.flight.api.model.Flight;
import org.ryanair.flight.api.model.RouteAPIResponseModel;
import org.ryanair.flight.api.model.ScheduleAPIResponseModel;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/3/24
 * Time: 3:32 PM
 */


public class TestUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_FILE_PATH = "src/test/java/org/ryanair/flight/api/context/util/json/";

    public static List<RouteAPIResponseModel> getRouteMockData() throws Exception {
        File file = ResourceUtils.getFile(BASE_FILE_PATH + "route_response.json");
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    public static List<Flight> getDirectFlightMockResponse() throws Exception {
        File file = ResourceUtils.getFile(BASE_FILE_PATH + "flight_data_1.json");
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    public static List<Flight> getIntDepartingFlightMockResponse() throws Exception {
        File file = ResourceUtils.getFile(BASE_FILE_PATH + "flight_data_1.json");
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    public static List<Flight> getIntArrivingFlightMockResponse() throws Exception {
        File file = ResourceUtils.getFile(BASE_FILE_PATH + "flight_data_2.json");
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    public static ScheduleAPIResponseModel getScheduledAPIMockResponse() throws Exception {
        File file = ResourceUtils.getFile(BASE_FILE_PATH + "schedule_response.json");
        return objectMapper.readValue(file, ScheduleAPIResponseModel.class);
    }
}
