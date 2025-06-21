package com.suraj.ecommerce.inventory_service.service;

import com.suraj.ecommerce.inventory_service.dto.OrderRequestDto;
import com.suraj.ecommerce.inventory_service.dto.OrderRequestItemDto;
import com.suraj.ecommerce.inventory_service.dto.ProductDto;
import com.suraj.ecommerce.inventory_service.entity.Product;
import com.suraj.ecommerce.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;

	public ProductDto getProductById(Long id) {
		log.info("Fetching product with ID: {}", id);
		Optional<Product> productInventory = productRepository.findById(id);
		return productInventory.map(item -> modelMapper.map(item, ProductDto.class))
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
	}

	public List<ProductDto> getAllInventory() {
		log.info("Fetching all products");
		List<Product> inventoryList = productRepository.findAll();
		return inventoryList.stream()
				.map(product -> modelMapper.map(product, ProductDto.class))
				.toList();
	}

	@Transactional
	public Double reduceStock(OrderRequestDto orderRequestDto) {
		log.info("Reducing stock for order request: {}", orderRequestDto);
		Double totalPrice = 0.0;
		for (OrderRequestItemDto item : orderRequestDto.getItems()) {
			Long productId = item.getProductId();
			Integer quantity = item.getQuantity();
			log.info("Processing item with product ID: {}, quantity: {}", productId, quantity);
			Product product = productRepository.findById(productId).orElseThrow();
			log.info("Product found: {}", product.getTitle());
			if (product.getStock() < quantity) {
				log.error("Insufficient stock for product ID: {}, available stock: {}, requested quantity: {}",
						productId, product.getStock(), quantity);
				throw new RuntimeException("Insufficient stock for product ID: " + productId);
			} else {
				product.setStock(product.getStock() - quantity);
				log.info("Updated stock for product ID: {}, new stock: {}", productId, product.getStock());
				totalPrice += (product.getPrice() * quantity);
				productRepository.save(product);
			}
		}
		return totalPrice;
	}

	@Transactional
	public Integer addStock(OrderRequestDto orderRequestDto) {
		log.info("Adding stock for order request: {}", orderRequestDto);
		Integer totalStockAdded = 0;
		for (OrderRequestItemDto item : orderRequestDto.getItems()) {
			Long productId = item.getProductId();
			Integer quantity = item.getQuantity();
			Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
			product.setStock(product.getStock() + quantity);
			totalStockAdded += quantity;
			productRepository.save(product);
			log.info("Updated stock for product ID: {}, new stock: {}", productId, product.getStock());
		}

		return totalStockAdded;
	}
}
