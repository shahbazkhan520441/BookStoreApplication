package com.book.store.application.repository;

import com.book.store.application.entity.Cart;
import com.book.store.application.service.CartService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {



}
