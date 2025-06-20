package com.suraj.ecommerce.order_service.entity;

import com.suraj.ecommerce.order_service.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private BigDecimal totalPrice;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
	private List<OrderItem> items;
}
