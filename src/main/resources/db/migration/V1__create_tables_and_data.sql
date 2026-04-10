CREATE TABLE menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(41) NOT NULL UNIQUE,
    table_number INT NOT NULL,
    status ENUM('PENDING', 'IN_PROGRESS', 'READY', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    tip DECIMAL(10, 2) DEFAULT 0.00,
    INDEX idx_order_id (order_id)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);

CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('WAITER', 'CHEF', 'MANAGER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT IGNORE INTO menu (name, description, price, category, active) VALUES
('Hambúrguer Artesanal', 'Pão brioche, blend bovino 180g, queijo cheddar e maionese.', 38.50, 'Lanches', TRUE),
('Batata Frita Especial', 'Porção de batatas crocantes com bacon e molho de queijo.', 25.00, 'Entradas', TRUE),
('Pizza Margherita', 'Molho de tomate, muçarela, manjericão fresco e azeite.', 55.00, 'Pizzas', TRUE),
('Suco de Laranja 500ml', 'Suco natural da fruta, sem adição de açúcar.', 12.00, 'Bebidas', TRUE),
('Petit Gâteau', 'Bolinho quente de chocolate com sorvete de baunilha.', 22.90, 'Sobremesas', TRUE),
('Salada Caesar', 'Alface americana, croutons, frango grelhado e molho especial.', 32.00, 'Saladas', TRUE),
('Refrigerante Lata', 'Coca-Cola ou Guaraná Antarctica 350ml.', 7.50, 'Bebidas', TRUE),
('Filé Mignon ao Molho Madeira', 'Acompanha arroz branco e purê de batatas.', 68.00, 'Pratos Principais', TRUE),
('Bruschetta de Tomate', 'Pão italiano tostado com tomates temperados e alho.', 18.00, 'Entradas', TRUE),
('Pudim de Leite', 'Receita tradicional com calda de caramelo.', 14.00, 'Sobremesas', TRUE);

COMMIT;