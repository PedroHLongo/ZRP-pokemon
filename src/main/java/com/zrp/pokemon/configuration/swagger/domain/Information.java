package com.zrp.pokemon.configuration.swagger.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "open-api.information")
@Configuration("openApiInfoProperties")
@Getter
@Setter
public class Information {
    private String title;
    private String version;
}
