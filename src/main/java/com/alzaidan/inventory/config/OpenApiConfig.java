

package com.alzaidan.inventory.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Al Zaidan Inventory API")
                        .description("REST API for the Al Zaidan Inventory Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Al Zaidan")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes(
                                "Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
    }
}

//@Configuration
//public class OpenApiConfig {
//
////    @Bean
////    public OpenAPI inventoryApi() {
////
////        return new OpenAPI()
////                .info(new Info()
////                        .title("Al Zaidan Inventory API")
////                        .version("1.0")
////                        .description("Inventory Management System REST API")
////                        .contact(new Contact()
////                                .name("Al Zaidan")
////                                .email("support@alzaidan.com")));
////    }
//
//
//    @Bean
//public OpenAPI inventoryApi() {
//
//    return new OpenAPI()
//            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
//            .components(new Components()
//                    .addSecuritySchemes("Bearer Authentication",
//                            new SecurityScheme()
//                                    .type(SecurityScheme.Type.HTTP)
//                                    .scheme("bearer")
//                                    .bearerFormat("JWT")))
//            .info(new Info()
//                    .title("Al Zaidan Inventory API")
//                    .version("1.0"));
//}
//
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//    http
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(auth -> auth
//            .requestMatchers(
//                "/v3/api-docs/**",
//                "/swagger-ui/**",
//                "/swagger-ui.html"
//            ).permitAll()
//            .anyRequest().authenticated()
//        );
//
//    return http.build();
//}
//}