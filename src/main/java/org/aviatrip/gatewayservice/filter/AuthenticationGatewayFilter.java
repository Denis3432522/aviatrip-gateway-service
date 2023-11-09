package org.aviatrip.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.aviatrip.gatewayservice.config.authorizer.RequestAuthorizer;
import org.aviatrip.gatewayservice.config.authorizer.SecurityRequirement;
import org.aviatrip.gatewayservice.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class AuthenticationGatewayFilter implements GatewayFilter {

    private final JwtUtil jwtUtil;
    private final RequestAuthorizer requestAuthorizer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        SecurityRequirement requirement = requestAuthorizer
                .getSecurityRequirements(exchange.getRequest().getPath().value(),
                        exchange.getRequest().getMethod());

        if(!requirement.requireAuthentication())
            return chain.filter(exchange);

        String token = parseToken(exchange.getRequest());
        Jws<Claims> claims = parseClaim(token);

        String subject = jwtUtil.getClaim("sub", claims)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied"));

        if(requirement.getRoles().length > 0)
            authorizeUser(claims, requirement.getRoles());

        ServerHttpRequest request = exchange.getRequest().mutate().header("subject", subject).build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private String parseToken(ServerHttpRequest request) {
        List<String> authList = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if(authList == null || authList.size() != 1 || authList.get(0) == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not authenticated");

        return authList.get(0).replaceFirst("Bearer ", "");
    }

    private Jws<Claims> parseClaim(String token) {
        try {
            return jwtUtil.parseClaims(token);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not authenticated");
        }
    }

    private void authorizeUser(Jws<Claims> claims, String[] authorizedRoles) {
        String role = jwtUtil.getClaim("role", claims)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied"));;

        if(Arrays.stream(authorizedRoles).noneMatch(s -> s.equals(role)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }
}
