-- ============================================================
-- ElectroHub - MySQL Database Initialization Script
-- ============================================================
-- Database: electrohub_db
-- Description: Table definitions for Products and Cart Items.
-- Note: Spring Data JPA automatically handles table creation/updates
--       via 'spring.jpa.hibernate.ddl-auto=update'.
-- ============================================================

CREATE DATABASE IF NOT EXISTS electrohub_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE electrohub_db;

-- 1. Products Table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    description VARCHAR(1000),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Shopping Cart Items Table
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_cart_items_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
