package io.idtx.pricing.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi pricingApiV1Group() {
        return GroupedOpenApi.builder().group("v1")
                .packagesToScan("io.idtx.pricing.api")
                .build();
    }

    @Bean
    public OpenAPI pricingAPI() {
        return new OpenAPI().info(new Info()
                .title("Price API")
                .description("Price Api")
                .version("1.0.0"));
    }
}
