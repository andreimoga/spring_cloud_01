package org.demo.cloud.microservice01;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
@Slf4j
public class ReactiveResilience4JConfig {

    /**
     * generic configuration for all REST calls
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> {
            /*
                id is MicroService02Client#getRemoteData() or MicroService02Client#getRemoteStreamFlux()
                see functions in MicroService02Client
             */
            log.info("Create Resilience Default Customizer for '{}'", id);
            return new Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build())
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .build();
        });
    }
}
