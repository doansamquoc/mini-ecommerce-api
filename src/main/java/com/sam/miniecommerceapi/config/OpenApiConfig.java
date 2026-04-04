package com.sam.miniecommerceapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OpenApiConfig {
    AppProperties properties;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(
            new Info().title(properties.getOpenApiTitle()).version(properties.getOpenApiVersion())
        ).servers(
            List.of(
                    new Server().url(properties.getOpenApiProdServer()).description("PROD"),
                    new Server().url("http://localhost:9000").description("LOCAL")
            )
        ).components(new Components().addSecuritySchemes("Bearer Token", new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"))
        ).security(List.of(new SecurityRequirement().addList("Bearer Token")));
    }
}
