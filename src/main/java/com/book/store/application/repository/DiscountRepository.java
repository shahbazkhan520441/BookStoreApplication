package com.book.store.application.repository;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Discount;
import com.book.store.application.mapper.BookMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long>{

    List<Discount> findByBookAndIsActiveTrue(Book book);
}
