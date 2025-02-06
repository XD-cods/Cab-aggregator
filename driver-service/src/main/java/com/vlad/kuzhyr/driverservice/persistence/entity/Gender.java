package com.vlad.kuzhyr.driverservice.persistence.entity;

import java.util.Arrays;
import lombok.Getter;


@Getter
public enum Gender {

    MALE(1),
    FEMALE(2),
    OTHER(3),
    UNKNOWN(4);

    private final int code;

    Gender(Integer code) {
        this.code = code;
    }

    public static Gender fromCode(int code) {
        return Arrays.stream(Gender.values())
            .filter(gender -> gender.code == code)
            .findFirst()
            .orElse(UNKNOWN);
    }

}
