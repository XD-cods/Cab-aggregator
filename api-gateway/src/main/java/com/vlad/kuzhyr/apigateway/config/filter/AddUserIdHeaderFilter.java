package com.vlad.kuzhyr.apigateway.config.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AddUserIdHeaderFilter extends AbstractGatewayFilterFactory<AddUserIdHeaderFilter.Config> {

    public AddUserIdHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
        private final String headerName = "X-User-Id";

        public String getHeaderName() {
            return headerName;
        }

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
            .map(ctx -> {
                Authentication auth = ctx.getAuthentication();
                if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                    String userId = jwt.getSubject();
                    ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header(config.getHeaderName(), userId)
                        .build();
                    return exchange.mutate().request(request).build();
                }
                return exchange;
            })
            .flatMap(chain::filter);
    }
}
