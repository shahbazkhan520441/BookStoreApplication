package com.book.store.application.controller;

import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.service.BookService;
import com.book.store.application.util.ResponseStructure;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    // Add a new book
    @PostMapping("/book")
    public ResponseEntity<ResponseStructure<BookResponse>> addBook( @RequestParam("quantity") int quantity,
                                                                    @RequestPart("bookImage") MultipartFile bookImage,
                                                                     @RequestPart("bookrequest")
                                                                        BookRequest bookRequest) throws IOException {
        System.out.println(bookRequest.getBookAuthor()+" "+" ========================="+bookRequest.getBookPrice());
        System.out.println("in conroller ----------------------------------------");
        return bookService.addBook(quantity,bookImage,bookRequest);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<ResponseStructure<BookResponse>>  updateBook(@PathVariable Long bookId,@RequestParam int quantity,@RequestPart("bookImage") MultipartFile bookImage,  @RequestPart("bookrequest")BookRequest bookRequest) throws IOException{
        return bookService.updateBook(bookId,quantity,bookImage,bookRequest);
    }


}