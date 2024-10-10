package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepositor extends JpaRepository<Seller,Long> {
}
