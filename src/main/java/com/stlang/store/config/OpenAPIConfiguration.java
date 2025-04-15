package com.stlang.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    private SecurityScheme createAPIKeySecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer");
    }

    private Server createServer(String url, String description) {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription(description);
        return server;
    }

    private Contact createContact() {
        Contact contact = new Contact();
        contact.setName("STlang");
        contact.setEmail("sondoquang3@gmail.com");
        contact.url("https://www.facebook.com/quang.son.414370");
        return contact;
    }

    private License createLicense() {
        return new License()
                .name("STlang Lisence")
                .url("https://choosealicense.com/license/mit/");
    }

    private Info createInfo() {
        return new Info()
                .title("Store Book API Documentation")
                .version("1.0")
                .contact(this.createContact())
                .description("Store Book API Documentation")
                .license(this.createLicense());
    }

    @Bean
    public OpenAPI createOpenAPI() {
        return new OpenAPI()
                .info(this.createInfo())
                .servers(List.of(
                        createServer("http://localhost:8080/", "Server URL in Development Environment")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication",createAPIKeySecurityScheme()));
    }

}
