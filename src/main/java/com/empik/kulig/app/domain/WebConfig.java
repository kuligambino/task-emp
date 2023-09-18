package com.empik.kulig.app.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.web.reactive.function.client.WebClient.builder;

@Configuration
class WebConfig {

    @Value("${url.github}")
    private String baseUrl;

    @Bean
    WebClient webClient() {
        return builder().baseUrl(baseUrl).build();
    }
}
