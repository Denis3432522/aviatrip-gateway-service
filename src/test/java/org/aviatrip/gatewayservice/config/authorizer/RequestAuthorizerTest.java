package org.aviatrip.gatewayservice.config.authorizer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.aviatrip.gatewayservice.config.authorizer.SecurityRequirement.withAuthentication;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestAuthorizerTest {

    static RequestAuthorizer requestAuthorizer;

    @BeforeAll
    public static void runUp() {
        requestAuthorizer = new RequestAuthorizerBuilder()
                .requestMatcher("/api/auth/signup").permitAll()
                .requestMatcher("/api/auth/token").permitAll()
                .requestMatcher("/api/representative/company", HttpMethod.POST).hasRoles("ROLE_REPRESENTATIVE")
                .requestMatcher("/api/representative/**").hasRoles("ROLE_VERIFIED_REPRESENTATIVE")
                .anyRequest(withAuthentication());
    }

    @Test
    public void testRequestAuthorizer() {
        Optional<RequestMatcher> matcher = requestAuthorizer.findRequestMatcher("/api/representative/company", HttpMethod.POST);

        assertTrue(matcher.isPresent());

        SecurityRequirement requirement = matcher.get().getSecurityRequirement();


        assertThat(requirement.getRoles()).contains("ROLE_REPRESENTATIVE")
                .hasSize(1);
    }
}
