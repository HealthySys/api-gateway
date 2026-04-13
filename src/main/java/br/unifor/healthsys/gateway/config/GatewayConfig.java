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
                .route("auth-service", r -> r
                        .path("/api/auth", "/api/auth/**")
                        .uri("lb://user-service"))

                .route("user-service", r -> r
                        .path("/api/users", "/api/users/**")
                        .uri("lb://user-service"))

                .route("patient-service", r -> r
                        .path("/api/patients", "/api/patients/**")
                        .uri("lb://patient-service"))

                .route("record-service", r -> r
                        .path("/api/records", "/api/records/**")
                        .uri("lb://record-service"))

                .route("triage-service", r -> r
                        .path("/api/triage", "/api/triage/**")
                        .uri("lb://triage-service"))

                .route("notification-service", r -> r
                        .path("/api/notifications", "/api/notifications/**", "/ws/**")
                        .uri("lb://notification-service"))

                .build();
    }
}
