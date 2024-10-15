package com.book.store.application.repository;

import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order,Long> {

    List<Order> findByCustomer(Customer customer);
}
