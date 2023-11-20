package org.aviatrip.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.aviatrip.gatewayservice.config.authorizer.RequestAuthorizer;
import org.aviatrip.gatewayservice.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;
    private final RequestAuthorizer requestAuthorizer;

    public AuthenticationFilter(JwtUtil jwtUtil, RequestAuthorizer requestAuthorizer) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.requestAuthorizer = requestAuthorizer;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new AuthenticationGatewayFilter(jwtUtil, requestAuthorizer);
    }

    public static class Config {
    }
}