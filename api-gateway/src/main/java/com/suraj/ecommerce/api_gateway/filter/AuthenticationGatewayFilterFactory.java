package com.suraj.ecommerce.api_gateway.filter;

import com.suraj.ecommerce.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

	private final JwtService jwtService;

	public AuthenticationGatewayFilterFactory(JwtService jwtService) {
		super(Config.class);
		this.jwtService = jwtService;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if(!config.isEnabled()) {
				return chain.filter(exchange);
			}

			String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
			if (authorizationHeader == null) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			if (!authorizationHeader.startsWith("Bearer ")) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}
			// Extract the token from the header
			String token = authorizationHeader.substring(7);

			Long userId = jwtService.getUserIdFromToken(token);

			// Mutate the request to include the userId in the headers
//			ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//					.header("X-User-Id", userId.toString())
//					.build();

			ServerWebExchange mutatedExchange = exchange.mutate()
					.request(exchange.getRequest().mutate()
							.header("X-User-Id", userId.toString())
							.build())
					.build();


			return chain.filter(mutatedExchange);
		});
	}

	@Data
	public static class Config {
		private boolean isEnabled;
	}

}
