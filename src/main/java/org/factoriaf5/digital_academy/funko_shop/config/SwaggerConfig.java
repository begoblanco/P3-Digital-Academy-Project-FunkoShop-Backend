package org.factoriaf5.digital_academy.funko_shop.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("funko_shop")
                .packagesToScan("org.factoriaf5.digital_academy.funko_shop")
                .build();
    }
}
