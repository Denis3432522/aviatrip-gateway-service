package org.aviatrip.gatewayservice.config.authorizer;

import org.springframework.http.HttpMethod;

import java.util.Objects;
import java.util.regex.Pattern;

public class RequestMatcher {

    private final Pattern pathPattern;

    private final HttpMethod[] methods;
    private final SecurityRequirement requirement;

    public RequestMatcher(String path, HttpMethod[] methods, SecurityRequirement requirement) {
        if(path == null || path.isBlank())
            throw new IllegalArgumentException("empty path");

        if(methods == null)
            throw new NullPointerException("http methods array null");

        if(requirement == null)
            throw new NullPointerException("security requirement null");

        this.pathPattern = Pattern.compile(formatPath(path));
        this.methods = methods;
        this.requirement = requirement;
    }

    private String formatPath(String path) {
        return path.replaceAll("(/\\*\\*)$", "(|[/?#].*)")
                .replaceAll("\\*\\*", "[^/]*");
    }

    public boolean matches(String path, HttpMethod method) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(path);

        return matchesByMethod(method) && matchesByPath(path);
    }

    private boolean matchesByMethod(HttpMethod method) {
        if(methods.length == 0)
            return true;

        for(HttpMethod httpMethod : methods) {
            if (httpMethod.equals(method))
                return true;
        }
        return false;
    }

    private boolean matchesByPath(String path) {
        return pathPattern.matcher(path).matches();
    }

    public SecurityRequirement getSecurityRequirement() {
        return requirement;
    }
}
