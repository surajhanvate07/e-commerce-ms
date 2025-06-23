package com.suraj.ecommerce.order_service.controller;

import com.suraj.ecommerce.order_service.dto.OrderRequestDto;
import com.suraj.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class OrdersController {

	private final OrderService orderService;

	@GetMapping("/hello-orders")
	public String helloOrderService() {
		return "Hello from Order Service";
	}

	@PostMapping("/create-order")
	public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
		OrderRequestDto orderRequest = orderService.createOrder(orderRequestDto);
		return ResponseEntity.ok(orderRequest);
	}

	@PostMapping("/cancel-order/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
		String cancelledOrderResponse = orderService.cancelOrder(id);
		return ResponseEntity.ok(cancelledOrderResponse);
	}

	@GetMapping
	public ResponseEntity<List<OrderRequestDto>> getAllOrders() {
		List<OrderRequestDto> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id) {
		OrderRequestDto order = orderService.getOrderById(id);
		return ResponseEntity.ok(order);
	}
}
