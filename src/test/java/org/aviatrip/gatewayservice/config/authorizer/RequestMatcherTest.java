package org.aviatrip.gatewayservice.config.authorizer;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;

class RequestMatcherTest {

    @Test
    void matches() {

        var requestMatcher = new RequestMatcher("/api/**", new HttpMethod[] {}, SecurityRequirement.permitAll());

        assertThrows(NullPointerException.class, () -> requestMatcher.matches("/api/fd2", null));
        assertThrows(NullPointerException.class, () -> requestMatcher.matches(null, HttpMethod.GET));

        assertFalse(requestMatcher.matches("", HttpMethod.GET));
        assertFalse(requestMatcher.matches("  ", HttpMethod.GET));
        assertFalse(requestMatcher.matches("/apis", HttpMethod.GET));
        assertFalse(requestMatcher.matches("foo/api", HttpMethod.GET));
        assertFalse(requestMatcher.matches("api", HttpMethod.POST));

        assertTrue(requestMatcher.matches("/api/", HttpMethod.PUT));
        assertTrue(requestMatcher.matches("/api?f=2", HttpMethod.GET));

        var requestMatcher2 = new RequestMatcher("/api/**", new HttpMethod[] {HttpMethod.GET, HttpMethod.DELETE}, SecurityRequirement.permitAll());

        assertFalse(requestMatcher2.matches("/api", HttpMethod.POST));
        assertFalse(requestMatcher2.matches("/api", HttpMethod.PUT));

        assertTrue(requestMatcher2.matches("/api", HttpMethod.GET));
        assertTrue(requestMatcher2.matches("/api", HttpMethod.DELETE));

        var requestMatcher3 = new RequestMatcher("/api/item", new HttpMethod[] {HttpMethod.POST}, SecurityRequirement.permitAll());

        assertFalse(requestMatcher3.matches("/api", HttpMethod.POST));
        assertFalse(requestMatcher3.matches("/api/item/id", HttpMethod.POST));

        assertFalse(requestMatcher3.matches("/api/item/", HttpMethod.POST));

        var requestMatcher4 = new RequestMatcher("/api/**/home", new HttpMethod[] {HttpMethod.POST}, SecurityRequirement.permitAll());

        assertTrue(requestMatcher4.matches("/api/person/home", HttpMethod.POST));
        assertTrue(requestMatcher4.matches("/api//home", HttpMethod.POST));
        assertTrue(requestMatcher4.matches("/api/f30g43/home", HttpMethod.POST));

        assertFalse(requestMatcher4.matches("/api/foo/bar/home", HttpMethod.POST));

        var requestMatcher5 = new RequestMatcher("/", new HttpMethod[] {HttpMethod.POST}, SecurityRequirement.permitAll());

        assertTrue(requestMatcher5.matches("/", HttpMethod.POST));

        assertFalse(requestMatcher5.matches("/some/thing?g=3", HttpMethod.POST));

        var requestMatcher6 = new RequestMatcher("/api/company/**/product/**", new HttpMethod[] {HttpMethod.POST}, SecurityRequirement.permitAll());

        assertTrue(requestMatcher6.matches("/api/company/team1/product/price", HttpMethod.POST));
        assertTrue(requestMatcher6.matches("/api/company/team1/product/", HttpMethod.POST));

        assertFalse(requestMatcher6.matches("/api/company/team/1/product/", HttpMethod.POST));

        var requestMatcher7 = new RequestMatcher("/k", new HttpMethod[] {HttpMethod.POST}, SecurityRequirement.withAuthentication());
        assertTrue(requestMatcher7.getSecurityRequirement().requireAuthentication());

        String[] roles = new String[] {"foo", "bar"};

        var requestMatcher8 = new RequestMatcher("/", new HttpMethod[] {HttpMethod.POST}, SecurityRequirement.ofRoles(roles));
        String[] roles2 = requestMatcher8.getSecurityRequirement().getRoles();

        assertArrayEquals(roles, roles2);
    }
}