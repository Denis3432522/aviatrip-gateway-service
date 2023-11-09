package org.aviatrip.gatewayservice.config.authorizer;

import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RequestAuthorizer {
    private final List<RequestMatcher> requestMatchers;
    private final SecurityRequirement fallbackSecurityRequirement;

    public RequestAuthorizer(List<RequestMatcher> requestMatchers, SecurityRequirement fallbackSecurityRequirement) {
        Objects.requireNonNull(requestMatchers);
        Objects.requireNonNull(fallbackSecurityRequirement);

        this.requestMatchers = requestMatchers;
        this.fallbackSecurityRequirement = fallbackSecurityRequirement;
    }

    public SecurityRequirement getSecurityRequirements(String path, HttpMethod method) {
        Optional<RequestMatcher> matcher = findRequestMatcher(path, method);

        return matcher.isPresent() ? matcher.get().getSecurityRequirement() :
                fallbackSecurityRequirement;
    };

    public Optional<RequestMatcher> findRequestMatcher(String path, HttpMethod method) {
        if(isEmptyPath(path) || method == null)
            return Optional.empty();

        for(RequestMatcher matcher : requestMatchers) {
            if(matcher.matches(path, method))
                return Optional.of(matcher);
        }
        return Optional.empty();
    }

    private boolean isEmptyPath(String path) {
        return path == null || path.isBlank();
    }
}







