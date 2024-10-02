package com.book.store.application.mapper;

import com.book.store.application.entity.Book;
import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class BookMapper {

    // Mapping BookRequest to Book entity for creating/updating a book
    public Book mapBookRequestToBook(BookRequest bookRequest, Book book) throws IOException {
        book.setBookName(bookRequest.getBookName());
        book.setBookAuthor(bookRequest.getBookAuthor());
        book.setBookDescription(bookRequest.getBookDescription());
        book.setBookPrice(bookRequest.getBookPrice());
        return book;
    }

    // Mapping Book entity to BookResponse for returning book details
    public BookResponse mapBookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getBookid())
                .bookName(book.getBookName())
                .bookAuthor(book.getBookAuthor())
                .bookDescription(book.getBookDescription())
                .bookPrice(book.getBookPrice())
                .bookQuantity(book.getBookQuantity())
                .build();
    }
}

