package com.vlad.kuzhyr.driverservice.config;

import com.vlad.kuzhyr.driverservice.utility.constant.Role;
import com.vlad.kuzhyr.driverservice.utility.mapper.converter.KeycloakJwtConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/", "/actuator/*").permitAll()
                .requestMatchers("/api/v1/drivers/me").hasRole(Role.DRIVER.getKeycloakName())
                .anyRequest().hasAnyRole(Role.ADMIN.getKeycloakName(), Role.SERVICE.getKeycloakName())
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtConverter()))
            )
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

