INSERT INTO orders (order_status, total_price) VALUES
('PENDING', 1999.98),     -- order_id 1
('CONFIRMED', 799.99),    -- order_id 2
('DELIVERED', 1999.95),   -- order_id 3
('CANCELLED', 3899.97),   -- order_id 4
('PENDING', 3599.96),     -- order_id 5
('DELIVERED', 999.99),    -- order_id 6
('CONFIRMED', 2199.98),   -- order_id 7
('CANCELLED', 3599.97),   -- order_id 8
('PENDING', 1599.99),     -- order_id 9
('DELIVERED', 279.98);    -- order_id 10

INSERT INTO order_item (product_id, quantity, order_id) VALUES
(1, 2, 1),
(3, 1, 2),
(5, 5, 3),
(7, 3, 4),
(2, 4, 5),
(9, 1, 6),
(12, 2, 7),
(6, 3, 8),
(14, 1, 9),
(15, 2, 10);

