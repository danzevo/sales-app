-- Seed users
INSERT INTO users (username, email, password, role, is_active, created_by)
VALUES
('admin', 'admin@example.com', '$2a$10$jd21zFzDlI0dxdQd3J2Bce.uXnY7M/jT9yHabzzdAR6syEp3dlW96', 'ADMIN', TRUE, 'system'),
('cashier1', 'cashier@example.com', '$2a$10$jd21zFzDlI0dxdQd3J2Bce.uXnY7M/jT9yHabzzdAR6syEp3dlW96', 'KASIR', FALSE, 'system');

-- Seed products
INSERT INTO products (name, stock, price, created_by)
VALUES
('Product A', 100, 50.00, 'admin'),
('Product B', 200, 30.00, 'admin');
