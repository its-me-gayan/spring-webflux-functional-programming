package org.ryanair.flight.api.util;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/29/24
 * Time: 6:36 PM
 */
public record Constant() {
    public static final String DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm";
    public static final String ROUTE_TYPE_DIRECT = "DIRECT";
    public static final String ROUTE_TYPE_INTER_CONNECTED = "INTER_CONNECTED";
    public static final String PROVIDER = "RYANAIR";
    public static final String DOWNSTREAM_SERVICE_NAME = "ryanair-api";

}
