package org.aviatrip.gatewayservice.config;

import org.aviatrip.gatewayservice.config.authorizer.RequestAuthorizer;
import org.aviatrip.gatewayservice.config.authorizer.RequestAuthorizerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import static org.aviatrip.gatewayservice.config.authorizer.SecurityRequirement.withAuthentication;

@Configuration
public class SecurityConfig {

    @Bean
    public RequestAuthorizer requestAuthorizer() {
        return new RequestAuthorizerBuilder()
                .requestMatcher("/api/auth/signup").permitAll()
                .requestMatcher("/api/auth/token").permitAll()
                .requestMatcher("/api/representative/company", HttpMethod.POST).hasRoles("ROLE_REPRESENTATIVE")
                .requestMatcher("/api/representative/**").hasRoles("ROLE_VERIFIED_REPRESENTATIVE")
                .anyRequest(withAuthentication());
    }
}