package com.vlad.kuzhyr.ratingservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateRatingRequest(

    @NotNull(message = "{validation.rating.null}")
    @Min(value = 0, message = "{validation.rating.small}")
    @Max(value = 5, message = "{validation.rating.over}")
    @Schema(description = "rating", example = "4.5")
    Double rating,

    @Schema(description = "comment of ride", example = "This ride was great!")
    String comment

) {
    @Override
    public String toString() {
        String truncatedComment = comment != null
                                  ? comment.substring(0, Math.min(comment.length(), 20))
                                  : null;

        return "UpdateRatingRequest{" +
               "rating=" + rating +
               ", comment='" + truncatedComment + '\'' +
               '}';
    }
}
