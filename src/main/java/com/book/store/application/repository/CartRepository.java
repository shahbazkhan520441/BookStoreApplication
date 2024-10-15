package com.book.store.application.repository;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Cart;
import com.book.store.application.service.CartService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {


    List<Cart> findByBook(Book book);
}

