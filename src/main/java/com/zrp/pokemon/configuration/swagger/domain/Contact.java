package com.zrp.pokemon.configuration.swagger.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "open-api.contact")
@Configuration("openApiContactProperties")
@Getter
@Setter
public class Contact {
    private String name;
    private String email;
}
