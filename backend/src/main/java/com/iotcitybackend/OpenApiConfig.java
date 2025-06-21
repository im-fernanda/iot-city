package com.iotcitybackend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    private static final List<String> TAG_ORDER = Arrays.asList(
        "Dispositivos",
        "Dados de Sensor",
        "Big Data"
    );

    @Bean
    public OpenApiCustomizer sortTagsCustomizer() {
        return openApi -> {
            openApi.setTags(openApi.getTags().stream()
                    .sorted(Comparator.comparingInt(tag -> TAG_ORDER.indexOf(tag.getName())))
                    .toList());
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("IoT City Backend API")
                        .description("API para plataforma de serviços interoperáveis IoT e IA para cidades inteligentes")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IoT City by Gilneide Fernanda")
                                .email("gilneidefernandaf@gmail.com")
                                .url(""))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ))
                .tags(List.of(
                        new Tag().name("Dispositivos").description("API para gerenciamento de dispositivos IoT"),
                        new Tag().name("Dados de Sensor").description("API para gerenciamento de dados de sensores IoT"),
                        new Tag().name("Big Data").description("APIs para processamento de Big Data com Spark")
                ));
    }
}