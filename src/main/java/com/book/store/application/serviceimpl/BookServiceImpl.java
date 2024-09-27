package com.book.store.application.serviceimpl;

import com.book.store.application.mapper.BookMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.service.BookService;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public ResponseEntity<ResponseStructure<BookResponse>> addBook(BookRequest bookRequest, MultipartFile bookLogo) {

        return null;

    }
}
