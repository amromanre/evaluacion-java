package com.backend.servicio.usuarios.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servicio de Usuarios API")
                        .description("API para la gestión de usuarios y teléfonos. Proyecto ejemplo con Spring Boot, EclipseLink y H2.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("backend@ejemplo.com")
                                .url("https://github.com/tu-org/servicio-usuarios"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación completa del proyecto y ejemplos de uso de la API REST de usuarios y teléfonos.")
                        .url("https://github.com/tu-org/servicio-usuarios"));
    }
}
