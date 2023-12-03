package org.aviatrip.gatewayservice.config.authorizer;

import lombok.Getter;

public class SecurityRequirement {

    private final boolean requireAuthentication;
    @Getter
    private final String[] roles;

    private SecurityRequirement(boolean requireAuthentication, String[] roles) {
        this.requireAuthentication = requireAuthentication;
        this.roles = roles;
    }

    public static SecurityRequirement permitAll() {
        return new SecurityRequirement(false, new String[0]);
    }

    public static SecurityRequirement ofRoles(String[] roles) {
        if(roles == null || roles.length == 0)
            throw new IllegalArgumentException("empty role array");

        return new SecurityRequirement(true, roles);
    }

    public static SecurityRequirement withAuthentication() {
        return new SecurityRequirement(true, new String[0]);
    }

    public boolean requireAuthentication() {
        return requireAuthentication;
    }

}
