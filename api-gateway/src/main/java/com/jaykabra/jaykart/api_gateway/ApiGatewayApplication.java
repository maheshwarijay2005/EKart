package com.jaykabra.jaykart.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> gatewayRouter() {
		return route("catalog-service")
			.route(path("/api/v1/products/**"), http())
			.filter(lb("catalog-service"))
			.build()
            .andRoute(path("/api/v1/orders/**"), http()).filter(lb("order-service"))
            .andRoute(path("/api/v1/payment/**"), http()).filter(lb("payment-service"))
            .andRoute(path("/api/v1/auth/**"), http()).filter(lb("auth-service"));
	}

}
