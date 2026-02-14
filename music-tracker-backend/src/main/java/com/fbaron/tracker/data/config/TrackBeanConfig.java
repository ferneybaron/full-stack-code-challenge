package com.fbaron.tracker.data.config;

import com.fbaron.tracker.core.repository.FileStorageRepository;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import com.fbaron.tracker.core.repository.TrackCommandRepository;
import com.fbaron.tracker.core.repository.TrackQueryRepository;
import com.fbaron.tracker.core.service.TrackService;
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

    @Bean
    public TrackService trackService(TrackQueryRepository trackQueryRepository,
                                     MusicProviderRepository musicProviderRepository,
                                     FileStorageRepository fileStorageRepository,
                                     TrackCommandRepository trackCommandRepository) {
        return new TrackService(
                trackQueryRepository,
                musicProviderRepository,
                fileStorageRepository,
                trackCommandRepository);
    }

}
