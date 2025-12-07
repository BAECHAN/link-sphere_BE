package com.example.linksphere_be.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
                .components(
                        Components()
                                .addSecuritySchemes(
                                        "bearer-key",
                                        SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(
                        Info().title("LinkSphere API")
                                .description("LinkSphere REST API documentation")
                                .version("1.0.0")
                )
                .addSecurityItem(SecurityRequirement().addList("bearer-key"))
    }
}
