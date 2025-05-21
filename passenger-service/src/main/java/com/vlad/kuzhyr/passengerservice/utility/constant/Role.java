package com.vlad.kuzhyr.passengerservice.utility.constant;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    SERVICE("SERVICE"),
    DRIVER("DRIVER"),
    PASSENGER("PASSENGER");

    private final String keycloakName;

    public static Set<String> getKeycloakNames() {
        return Arrays.stream(values())
            .map(Role::getKeycloakName)
            .collect(Collectors.toSet());
    }

    public String asSpringRole() {
        return "ROLE_" + this.name();
    }

}
