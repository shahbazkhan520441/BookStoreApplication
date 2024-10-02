package com.book.store.application.repository;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByBook(Book product);
}