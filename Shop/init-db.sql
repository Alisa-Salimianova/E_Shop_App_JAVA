-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);
CREATE INDEX IF NOT EXISTS idx_products_rating ON products(rating DESC);
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);

-- Insert sample data
INSERT INTO products (name, description, price, category, manufacturer, sku, stock_quantity, rating, rating_count, active, created_at, updated_at)
VALUES
    ('iPhone 15 Pro', 'Latest Apple smartphone', 999.99, 'ELECTRONICS', 'Apple', 'IPHONE15-PRO', 50, 4.8, 1200, true, NOW(), NOW()),
    ('MacBook Air M2', 'Apple laptop with M2 chip', 1199.99, 'ELECTRONICS', 'Apple', 'MBA-M2', 30, 4.7, 850, true, NOW(), NOW()),
    ('Nike Air Max', 'Running shoes', 129.99, 'SPORTS', 'Nike', 'NIKE-AIRMAX', 200, 4.5, 2300, true, NOW(), NOW()),
    ('Design Patterns Book', 'Gang of Four design patterns', 49.99, 'BOOKS', 'Addison-Wesley', 'DP-BOOK', 100, 4.9, 1500, true, NOW(), NOW()),
    ('Coffee Maker', 'Automatic coffee machine', 89.99, 'HOME', 'Philips', 'PHILIPS-CM', 75, 4.3, 420, true, NOW(), NOW())
ON CONFLICT (sku) DO NOTHING;