package com.bankingSystem.customer_ms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for setting up the {@link RestTemplate} bean.
 * <p>
 * This class is annotated with {@link Configuration} to indicate that it contains
 * bean definitions for the Spring application context. The {@link RestTemplate} bean
 * is used for making HTTP requests to external services.
 * </p>
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates and provides a {@link RestTemplate} bean to the Spring application context.
     *
     * @return a new instance of {@link RestTemplate} to be used for making HTTP requests.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
