package com.zrp.pokemon.configuration.swagger.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "open-api")
@Configuration("openApiProperties")
@Getter
@Setter
public class OpenApiProperties {
    private Server server;
    private Contact contact;
    private Information information;
}
