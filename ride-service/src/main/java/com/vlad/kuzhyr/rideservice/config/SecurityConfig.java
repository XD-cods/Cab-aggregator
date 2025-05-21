package com.vlad.kuzhyr.rideservice.config;

import com.vlad.kuzhyr.rideservice.utility.constant.Role;
import com.vlad.kuzhyr.rideservice.utility.mapper.converter.KeycloakJwtConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers("/actuator/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/v1/rides").hasAnyRole(
                    Role.PASSENGER.getKeycloakName(),
                    Role.ADMIN.getKeycloakName(),
                    Role.SERVICE.getKeycloakName())

                .requestMatchers(HttpMethod.PATCH, "/api/v1/rides/*/assign-driver").hasAnyRole(
                    Role.DRIVER.getKeycloakName(),
                    Role.ADMIN.getKeycloakName(),
                    Role.SERVICE.getKeycloakName())

                .requestMatchers(HttpMethod.GET, "/api/v1/rides").authenticated()

                .requestMatchers(HttpMethod.GET, "/api/v1/rides/**").authenticated()

                .requestMatchers(HttpMethod.PUT, "/api/v1/rides/**").hasAnyRole(
                    Role.ADMIN.getKeycloakName(),
                    Role.SERVICE.getKeycloakName())
                .requestMatchers(HttpMethod.PATCH, "/api/v1/rides/**").hasAnyRole(
                    Role.ADMIN.getKeycloakName(),
                    Role.SERVICE.getKeycloakName())

                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtConverter())))
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
