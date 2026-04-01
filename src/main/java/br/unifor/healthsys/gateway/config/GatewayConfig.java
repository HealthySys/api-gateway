package br.unifor.healthsys.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Rota para user-service
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("user-service-cb")
                                        .setFallbackUri("forward:/fallback/user-service"))
                                .stripPrefix(1))
                        .uri("lb://user-service"))

                // Rota para patient-service
                .route("patient-service", r -> r
                        .path("/api/patients/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("patient-service-cb")
                                        .setFallbackUri("forward:/fallback/patient-service"))
                                .stripPrefix(1))
                        .uri("lb://patient-service"))

                // Rota para record-service
                .route("record-service", r -> r
                        .path("/api/records/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("record-service-cb")
                                        .setFallbackUri("forward:/fallback/record-service"))
                                .stripPrefix(1))
                        .uri("lb://record-service"))

                // Rota para triage-service
                .route("triage-service", r -> r
                        .path("/api/triage/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("triage-service-cb")
                                        .setFallbackUri("forward:/fallback/triage-service"))
                                .stripPrefix(1))
                        .uri("lb://triage-service"))

                // Rota para notification-service
                .route("notification-service", r -> r
                        .path("/api/notifications/**", "/ws/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("notification-service-cb")
                                        .setFallbackUri("forward:/fallback/notification-service"))
                                .stripPrefix(1))
                        .uri("lb://notification-service"))

                .build();
    }
}
