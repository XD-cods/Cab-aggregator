package com.vlad.kuzhyr.ratingservice.web.response;

public record RatingResponse(

        Long id,
        Long driverId,
        Long rideId,
        Long passengerId,
        Float rating,
        String comment

) {
}