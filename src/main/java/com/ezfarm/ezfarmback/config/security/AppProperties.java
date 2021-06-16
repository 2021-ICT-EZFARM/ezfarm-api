package com.ezfarm.ezfarmback.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private final Auth auth = new Auth();

    @Data
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }
}
