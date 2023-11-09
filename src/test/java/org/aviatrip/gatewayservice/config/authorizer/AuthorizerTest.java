package org.aviatrip.gatewayservice.config.authorizer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizerTest {

    @Test
    void authorize() {

        RequestAuthorizer authorizer = new RequestAuthorizerBuilder()
                .requestMatcher("/auth/signup/**", HttpMethod.POST).permitAll()
                .requestMatcher("/auth/signin", HttpMethod.POST).permitAll()
                .requestMatcher("/representative/**").hasRoles("REPRESENTATIVE")
                .requestMatcher("/reference/**").permitAll()
                .requestMatcher("/tickets/**").permitAll()
                .anyRequest(SecurityRequirement.withAuthentication());

        var requirement = authorizer.getSecurityRequirements("/auth/signup/**", HttpMethod.POST);
        assertFalse(requirement.requireAuthentication());
        assertEquals(0, requirement.getRoles().length);

        var requirement2 = authorizer.getSecurityRequirements("/representative/flight/2/seats", HttpMethod.PUT);
        assertTrue(requirement2.requireAuthentication());
        assertEquals("REPRESENTATIVE", requirement2.getRoles()[0]);

        var requirement3 = authorizer.getSecurityRequirements("/jets/2", HttpMethod.DELETE);
        assertTrue(requirement3.requireAuthentication());
        assertEquals(0, requirement3.getRoles().length);
    }

    @Test
    void authorize2() {

        RequestAuthorizer authorizer = new RequestAuthorizerBuilder()
                .requestMatcher("/company/teams/2").permitAll()
                .requestMatcher("/company/**").authenticated()
                .anyRequest(SecurityRequirement.permitAll());

        var requirement = authorizer.getSecurityRequirements("/company/teams/2", HttpMethod.POST);
        assertFalse(requirement.requireAuthentication());

        var requirement2 = authorizer.getSecurityRequirements("/company/teams/22", HttpMethod.POST);
        assertTrue(requirement2.requireAuthentication());
    }
}
