package com.vlad.kuzhyr.ratingservice.persistence.entity;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RatedBy {

    PASSENGER(1),
    DRIVER(2);

    private final int code;

    RatedBy(int code) {
        this.code = code;
    }

    public static RatedBy fromCode(int code) {
        return Arrays.stream(RatedBy.values())
            .filter(ratedBy -> ratedBy.code == code)
            .findFirst()
            .orElse(null);
    }

}
