package com.electrohub.repository;

import com.electrohub.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for CartItem entity.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductId(Long productId);

    void deleteByProductId(Long productId);
}
