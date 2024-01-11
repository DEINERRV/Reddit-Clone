package com.deinerrv.RedditClone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Deiner Rodriguez V",
            url = "https://portfolio-git-main-deinerrv.vercel.app/#contact",
            email = "deiner.ra10@gmai.com"
        ),
        description = "Documentation for a Reddit Back-end Clone",
        title = "Reddit Clone"
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Local Env"
        )
    },
    security = @SecurityRequirement(
        name = "Bearer Auth"
    )
)
@SecurityScheme(
    scheme = "bearer",
    name = "Bearer Auth",
    description = "JWT",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    
}
