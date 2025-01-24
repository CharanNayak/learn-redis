package com.example.learn_redis.basic;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.MicrometerTracing;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfiguration {


    @Bean
    public ClientResources clientResources(ObservationRegistry observationRegistry) {

        return ClientResources.builder()
                .tracing(new MicrometerTracing(observationRegistry, "my-redis-cache"))
                .build();
    }

}
