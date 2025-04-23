package com.vlad.kuzhyr.authservice.utility.mapper.converter;


import com.vlad.kuzhyr.authservice.utility.constant.Role;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class KeycloakJwtConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        return new JwtAuthenticationToken(
            jwt,
            extractValidRoles(jwt),
            jwt.getSubject()
        );
    }

    private Collection<GrantedAuthority> extractValidRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return List.of();
        }

        Set<String> validRoleNames = Role.getKeycloakNames();
        List<String> tokenRoles = (List<String>) realmAccess.get("roles");

        return tokenRoles.stream()
            .filter(validRoleNames::contains)
            .map(role -> Role.valueOf(role.toUpperCase()).asSpringRole())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

}
