package com.book.store.application.repository;

import com.book.store.application.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepositor extends JpaRepository<Seller,Long> {
}
