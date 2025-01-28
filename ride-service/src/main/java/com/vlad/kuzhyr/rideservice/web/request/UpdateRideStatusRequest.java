package com.vlad.kuzhyr.rideservice.web.request;

import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateRideStatusRequest(

        @NotNull(message = "{validation.status.empty}")
        RideStatus rideStatus

) {
}
