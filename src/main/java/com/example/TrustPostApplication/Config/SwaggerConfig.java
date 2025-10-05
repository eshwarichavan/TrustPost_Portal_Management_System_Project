package com.example.TrustPostApplication.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI().info(
                        //for adding title & sub title
                        new Info()
                                .title("TrustPost Portal Managmenet System")
                                .description("By Eshwari Chavan & Mitali Rote")

                )

                //for adding local host links
                .servers(List.of(new io.swagger.v3.oas.models.servers.Server()
                                .url("http://localhost:8080").description("local"),
                        new Server()
                                //if above link doesn't work use this
                                .url("http://localhost:8081").description("Live")))


                //for ordering the controllers
                .tags(Arrays.asList(
                        new Tag().name("Authentication API"),   //gives priority to this controller first
                        new Tag().name("Users API"),
                        new Tag().name("Posts API"),
                        new Tag().name("Comments API")
                ))

                //to add security to each API and its associated endpoints.
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
