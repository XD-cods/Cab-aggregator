package com.vlad.kuzhyr.authservice.utility.constant;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessRole {
    PASSENGER("PASSENGER"),
    DRIVER("DRIVER");

    private final String keycloakName;

    public static Set<String> getKeycloakNames() {
        return Arrays.stream(values())
            .map(BusinessRole::getKeycloakName)
            .collect(Collectors.toSet());
    }

}