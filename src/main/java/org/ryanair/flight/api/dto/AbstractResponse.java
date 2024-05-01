package org.ryanair.flight.api.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 3/31/24
 * Time: 3:35 PM
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405172041950251807L;
    private String timeStamp;
    private int responseCode;
    private String message;
    private String messageDescription;
    private Object data;
}
