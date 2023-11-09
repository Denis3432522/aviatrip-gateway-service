package org.aviatrip.gatewayservice.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtProperties {

    @NotNull
    private String secret;

    @NotNull
    private long tokenExpiration;
}
