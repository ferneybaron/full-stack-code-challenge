package com.fbaron.tracker.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TrackBeanConfig {

    @Bean
    public RestClient spotifyRestClient(RestClient.Builder builder,
                                        @Value("${spotify.api-base-url}") String apiBaseUrl) {
        return builder
                .baseUrl(apiBaseUrl)
                .build();
    }

}
