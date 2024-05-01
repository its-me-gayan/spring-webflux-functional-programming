package org.ryanair.flight.api.util;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/31/24
 * Time: 9:06 PM
 */
public record ResponseMessage() {
    public static final String ERR_MSG_NO_VALID_ROUTE_FOUND ="No Valid Route found for given IATA combination";
    public static final String ERR_INVALID_REQ_PARAMETERS ="Invalid request parameters";
    public static final String ERR_INVALID_RESP_FROM_BACKEND ="Invalid Response from backend";
    public static final String ERR_SERVICE_UNAVAILABLE ="Service Unavailable";


    public static final String RESPONSE_MESSAGE_SUCCESS = "Data retrieved successfully";
    public static final String RESPONSE_MESSAGE_NO_CONTENT = "No any related flights Found for the the given criteria";
    public static final String RESPONSE_MESSAGE_FAILED = "Data retrieved Failed";
    public static final String RESPONSE_DESCRIPTION_INFO = " With %s Direct flights and %s Interconnect flights with one stop for the given criteria";

}
