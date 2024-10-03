package com.zrp.pokemon.configuration.swagger.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "open-api.server")
@Configuration("openApiServerProperties")
@Getter
@Setter
public class Server {
    private String url;
    private String description;
}