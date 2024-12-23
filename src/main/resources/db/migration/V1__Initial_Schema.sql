CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN', 'KASIR')),
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    changed_at TIMESTAMP,
    changed_by VARCHAR(50),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(50)
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL DEFAULT '',
    stock INT NOT NULL DEFAULT 0,
    price DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    changed_at TIMESTAMP,
    changed_by VARCHAR(50),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(50)
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    cashier_id INT NOT NULL REFERENCES users(id),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

CREATE TABLE transaction_details (
    id SERIAL PRIMARY KEY,
    transaction_id UUID REFERENCES transactions(id),
    product_id INT REFERENCES products(id),
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);