package org.aviatrip.gatewayservice.config;

import org.aviatrip.gatewayservice.config.authorizer.RequestAuthorizer;
import org.aviatrip.gatewayservice.config.authorizer.RequestAuthorizerBuilder;
import org.aviatrip.gatewayservice.config.properties.JwtProperties;
import org.aviatrip.gatewayservice.util.JwtUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.aviatrip.gatewayservice.config.authorizer.SecurityRequirement.withAuthentication;

@Configuration
public class SecurityConfig {

    @Bean
    public RequestAuthorizer requestAuthorizer() {
        return new RequestAuthorizerBuilder()
                .requestMatcher("/api/auth/signup").permitAll()
                .requestMatcher("/api/auth/token").permitAll()
                .anyRequest(withAuthentication());
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtProperties());
    }

    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
}
