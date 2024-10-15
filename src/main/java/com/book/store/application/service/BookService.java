package com.book.store.application.service;

import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    ResponseEntity<ResponseStructure<BookResponse>> addBook(int quantity, MultipartFile bookImage, BookRequest bookRequest) throws IOException;

    ResponseEntity<ResponseStructure<BookResponse>> updateBook(Long bookId, int quantity, MultipartFile bookImage, BookRequest bookRequest) throws IOException;

    ResponseEntity<ResponseStructure<BookResponse>> findBook(Long bookId);

    ResponseEntity<ResponseStructure<List<BookResponse>>> findBooks();


    ResponseEntity<ResponseStructure<BookResponse>> updateBookQuantity(Long bookId, int quantity);
}
