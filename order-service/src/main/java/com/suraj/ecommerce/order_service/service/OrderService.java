package com.suraj.ecommerce.order_service.service;

import com.suraj.ecommerce.order_service.client.InventoryFeignClient;
import com.suraj.ecommerce.order_service.dto.OrderRequestDto;
import com.suraj.ecommerce.order_service.entity.OrderItem;
import com.suraj.ecommerce.order_service.entity.Orders;
import com.suraj.ecommerce.order_service.enums.OrderStatus;
import com.suraj.ecommerce.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final ModelMapper modelMapper;
	private final InventoryFeignClient inventoryFeignClient;

	public List<OrderRequestDto> getAllOrders() {
		log.info("Fetching all orders");
		List<Orders> ordersList = orderRepository.findAll();
		return ordersList.stream().map(order -> modelMapper.map(order, OrderRequestDto.class))
				.toList();
	}

	public OrderRequestDto getOrderById(Long id) {
		log.info("Fetching order with ID: {}", id);
		Orders order = orderRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
		return modelMapper.map(order, OrderRequestDto.class);
	}

//	@Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
	@CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
	@RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
	public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
		log.info("Creating order");
		Double totalPrice = inventoryFeignClient.reduceStock(orderRequestDto);

		Orders orders = modelMapper.map(orderRequestDto, Orders.class);
		for (OrderItem orderItem : orders.getItems()) {
			orderItem.setOrder(orders);
		}
		orders.setTotalPrice(BigDecimal.valueOf(totalPrice));
		orders.setOrderStatus(OrderStatus.CONFIRMED);

		Orders savedOrders = orderRepository.save(orders);
		return modelMapper.map(savedOrders, OrderRequestDto.class);
	}

	public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
		log.error("Failed to create order {} :", throwable.getLocalizedMessage());
		return new OrderRequestDto();
	}
}
