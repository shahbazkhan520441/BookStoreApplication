package com.book.store.application.repository;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByBook(Book product);
}