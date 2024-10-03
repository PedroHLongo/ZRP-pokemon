package com.zrp.pokemon.configuration.swagger;

import com.zrp.pokemon.configuration.swagger.domain.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {
    private final OpenApiProperties properties;

    @Bean
    public OpenAPI api() {
        final Server server = new Server()
                .url(properties.getServer().getUrl())
                .description(properties.getServer().getDescription());

        final Contact contact = new Contact()
                .name(properties.getContact().getName())
                .email(properties.getContact().getEmail());

        final Info information = new Info()
                .title(properties.getInformation().getTitle())
                .version(properties.getInformation().getVersion())
                .contact(contact);

        return new OpenAPI().info(information).servers(List.of(server));
    }
}