package com.hackernews.taazakhabar.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    @Value("${taazakhabar.client.hackernews.baseuri}")
    private String baseUri;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .rootUri(baseUri)
                .build();
    }
}
