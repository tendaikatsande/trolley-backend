package zw.co.trolley.ApiGateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8081"))

                // Product Service Routes
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .uri("http://localhost:8082"))

                // Order Service Routes
                .route("order-service", r -> r
                        .path("/api/orders/**", "/api/cart/**")
                        .uri("http://localhost:8083"))

                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("http://localhost:8084"))

                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .uri("http://localhost:8085"))

                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("http://localhost:8086"))
                .build();
    }
}
