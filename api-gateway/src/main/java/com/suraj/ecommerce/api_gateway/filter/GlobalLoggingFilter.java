package com.suraj.ecommerce.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalLoggingFilter implements GlobalFilter, Ordered {
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		// Pre-filer
		log.info("Logging from Global Pre: {}", exchange.getRequest().getURI());
		return chain.filter(exchange)
				.doOnError(error -> log.error("Exception in downstream processing", error))
				.then(Mono.fromRunnable(() -> {
					log.info("Logging from Global Post: {}", exchange.getResponse().getStatusCode());
				}));
	}

	@Override
	public int getOrder() {
		return 5;
	}
}
