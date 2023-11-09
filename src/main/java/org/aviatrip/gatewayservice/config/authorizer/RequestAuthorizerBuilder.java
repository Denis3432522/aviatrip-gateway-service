package org.aviatrip.gatewayservice.config.authorizer;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestAuthorizerBuilder {

    private final List<RequestMatcher> matchers = new ArrayList<>();

    public RequestMatcherBuilder requestMatcher(String path, HttpMethod ...methods) {
        return new RequestMatcherBuilder(path, methods);
    }

    public RequestAuthorizer anyRequest(SecurityRequirement requirement) {
        Objects.requireNonNull(requirement);
        return new RequestAuthorizer(matchers, requirement);
    }

    public class RequestMatcherBuilder {

        private final String path;
        private final HttpMethod[] methods;

        private boolean isUsed;

        private RequestMatcherBuilder(String path, HttpMethod... methods) {
            this.path = path;
            this.methods = methods;
        }

        public RequestAuthorizerBuilder authenticated() {
            assertNotUsed();
            matchers.add(build(SecurityRequirement.withAuthentication()));

            return RequestAuthorizerBuilder.this;
        }

        public RequestAuthorizerBuilder hasRoles(String... roles) {
            assertNotUsed();
            matchers.add(build(SecurityRequirement.ofRoles(roles)));

            return RequestAuthorizerBuilder.this;
        }
        public RequestAuthorizerBuilder permitAll() {
            assertNotUsed();
            matchers.add(build(SecurityRequirement.permitAll()));

            return RequestAuthorizerBuilder.this;
        }

        private RequestMatcher build(SecurityRequirement requirement) {
            isUsed = true;

            return new RequestMatcher(path, methods, requirement);
        }

        private void assertNotUsed() {
            if(isUsed)
                throw new RuntimeException("multiple constructions is forbidden");
        }
    }
}

