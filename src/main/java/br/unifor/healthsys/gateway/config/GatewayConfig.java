package br.unifor.healthsys.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final int REPLENISH_RATE = 20;
    private static final int BURST_CAPACITY = 40;

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String hostAddress = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
            return reactor.core.publisher.Mono.just(hostAddress);
        };
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(REPLENISH_RATE, BURST_CAPACITY);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                           KeyResolver ipKeyResolver,
                                           RedisRateLimiter redisRateLimiter) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth", "/api/auth/**")
                        .uri("lb://user-service"))

                .route("user-service", r -> r
                        .path("/api/users", "/api/users/**")
                        .filters(f -> f
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .circuitBreaker(config -> config
                                        .setName("user-service-cb")
                                        .setFallbackUri("forward:/fallback/user-service")))
                        .uri("lb://user-service"))

                .route("patient-service", r -> r
                        .path("/api/patients", "/api/patients/**")
                        .filters(f -> f
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .circuitBreaker(config -> config
                                        .setName("patient-service-cb")
                                        .setFallbackUri("forward:/fallback/patient-service")))
                        .uri("lb://patient-service"))

                .route("record-service", r -> r
                        .path("/api/records", "/api/records/**")
                        .filters(f -> f
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .circuitBreaker(config -> config
                                        .setName("record-service-cb")
                                        .setFallbackUri("forward:/fallback/record-service")))
                        .uri("lb://record-service"))

                .route("triage-service", r -> r
                        .path("/api/triage", "/api/triage/**")
                        .filters(f -> f
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .circuitBreaker(config -> config
                                        .setName("triage-service-cb")
                                        .setFallbackUri("forward:/fallback/triage-service")))
                        .uri("lb://triage-service"))

                .route("notification-service", r -> r
                        .path("/api/notifications", "/api/notifications/**")
                        .filters(f -> f
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .circuitBreaker(config -> config
                                        .setName("notification-service-cb")
                                        .setFallbackUri("forward:/fallback/notification-service")))
                        .uri("lb://notification-service"))

                .route("notification-ws", r -> r
                        .path("/ws/**")
                        .uri("lb:ws://notification-service"))

                .build();
    }
}
