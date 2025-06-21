package com.suraj.ecommerce.inventory_service.controller;

import com.suraj.ecommerce.inventory_service.client.OrdersFeignClient;
import com.suraj.ecommerce.inventory_service.dto.OrderRequestDto;
import com.suraj.ecommerce.inventory_service.dto.ProductDto;
import com.suraj.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final DiscoveryClient discoveryClient;
	private final RestClient restClient;
	private final OrdersFeignClient ordersFeignClient;

	// fetch orders from Order Service
	@GetMapping("/fetch-orders")
	public String fetchFromOrdersService() {
//		ServiceInstance orderServiceInstance = discoveryClient.getInstances("order-service").stream()
//				.findFirst()
//				.orElseThrow(() -> new RuntimeException("Order Service not found"));

//		return restClient.get().uri(orderServiceInstance.getUri()+"/orders/core/hello-orders")
//				.retrieve().body(String.class);

		return ordersFeignClient.helloOrdersService();
	}

	@GetMapping
	public ResponseEntity<List<ProductDto>> getAllProductsInventory() {
		log.info("Fetching all products");
		List<ProductDto> productsInventory = productService.getAllInventory();
		return ResponseEntity.ok(productsInventory);
	}


	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
		ProductDto product = productService.getProductById(id);
		return ResponseEntity.status(HttpStatus.OK).body(product);
	}

	@PutMapping("/reduce-stock")
	public ResponseEntity<Double> reduceStock(@RequestBody OrderRequestDto orderRequestDto) {
		Double totalPrice = productService.reduceStock(orderRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(totalPrice);
	}

	@PutMapping("/add-stock")
	public ResponseEntity<Integer> addStock(@RequestBody OrderRequestDto orderRequestDto) {
		log.info("Adding stock for product: {}", orderRequestDto);
		Integer updated = productService.addStock(orderRequestDto);
		return ResponseEntity.ok(updated);
	}
}
