package br.unifor.healthsys.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final String USER_SERVICE_URI = "http://user-service:8081";
    private static final String PATIENT_SERVICE_URI = "http://patient-service:8082";
    private static final String RECORD_SERVICE_URI = "http://record-service:8083";
    private static final String TRIAGE_SERVICE_URI = "http://triage-service:8084";
    private static final String NOTIFICATION_SERVICE_URI = "http://notification-service:8085";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Em ambiente local com Docker Compose, falar direto com os servicos
                // deixa o bootstrap mais previsivel do que depender do registry logo no inicio.
                .route("auth-service", r -> r
                        .path("/api/auth", "/api/auth/**")
                        .uri(USER_SERVICE_URI))

                .route("user-service", r -> r
                        .path("/api/users", "/api/users/**")
                        .uri(USER_SERVICE_URI))

                .route("patient-service", r -> r
                        .path("/api/patients", "/api/patients/**")
                        .uri(PATIENT_SERVICE_URI))

                .route("record-service", r -> r
                        .path("/api/records", "/api/records/**")
                        .uri(RECORD_SERVICE_URI))

                .route("triage-service", r -> r
                        .path("/api/triage", "/api/triage/**")
                        .uri(TRIAGE_SERVICE_URI))

                .route("notification-service", r -> r
                        .path("/api/notifications", "/api/notifications/**", "/ws/**")
                        .uri(NOTIFICATION_SERVICE_URI))

                .build();
    }
}
