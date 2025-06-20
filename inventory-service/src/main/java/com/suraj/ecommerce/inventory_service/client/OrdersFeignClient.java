package com.suraj.ecommerce.inventory_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service" , path = "/orders")
public interface OrdersFeignClient {

	@GetMapping("/core/hello-orders")
	String helloOrdersService();
}
