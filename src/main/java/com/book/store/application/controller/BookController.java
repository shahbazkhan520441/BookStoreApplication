package com.book.store.application.controller;

import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.service.BookService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    // Add a new book
    @PostMapping("/book")
    public ResponseEntity<ResponseStructure<BookResponse>> addBook(@RequestPart("book") BookRequest bookRequest,
                                                                   @RequestPart("logo") MultipartFile bookLogo) throws IOException {
        return bookService.addBook(bookRequest, bookLogo);
    }
}