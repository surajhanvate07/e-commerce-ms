package com.suraj.ecommerce.order_service.client;

import com.suraj.ecommerce.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "inventory-service", path = "/inventory")
public interface InventoryFeignClient {

	@PutMapping("/products/reduce-stock")
	Double reduceStock(@RequestBody OrderRequestDto orderRequestDto);

	@PutMapping("/products/add-stock")
	Integer addStock(@RequestBody OrderRequestDto orderRequestDto);
}
