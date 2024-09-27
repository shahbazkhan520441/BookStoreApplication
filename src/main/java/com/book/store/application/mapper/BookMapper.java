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
    public Book mapBookRequestToBook(BookRequest bookRequest, Book book, MultipartFile bookLogo) throws IOException {
        byte[] logoBytes = bookLogo != null ? bookLogo.getBytes() : null;  // Convert logo file to byte array
        book.setBookName(bookRequest.getBookName());
        book.setBookAuthor(bookRequest.getBookAuthor());
        book.setBookDescription(bookRequest.getBookDescription());
        book.setBookLogo(logoBytes);  // Handle the logo bytes
        book.setBookPrice(bookRequest.getBookPrice());
        book.setBookQuantity(bookRequest.getBookQuantity());
        return book;
    }

    // Mapping Book entity to BookResponse for returning book details
    public BookResponse mapBookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getBookid())
                .bookName(book.getBookName())
                .bookAuthor(book.getBookAuthor())
                .bookDescription(book.getBookDescription())
                .bookLogo(book.getBookLogo())  // Return the logo bytes
                .bookPrice(book.getBookPrice())
                .bookQuantity(book.getBookQuantity())
                .build();
    }
}

