package com.book.store.application.mapper;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Discount;
import com.book.store.application.entity.Image;
import com.book.store.application.repository.DiscountRepository;
import com.book.store.application.repository.ImageRepository;
import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.responsedto.BookResponseCart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class BookMapper {

    private final ImageRepository imageRepository;
    private final DiscountRepository discountRepository;

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
        List<Image> images = imageRepository.findByBook(book);
        String image = images.isEmpty() ?
                "https://bableshaazad.com/static/media/bob.a2ca1510718f08b5313d.jpg"
                : images.getFirst().getImage();

        List<Discount> discounts = discountRepository.findByBookAndIsActiveTrue(book);
        Double discount = discounts.isEmpty() ? 0.0 : discounts.getFirst().getDiscountValue();
        return BookResponse.builder()
                .id(book.getBookid())
                .bookName(book.getBookName())
                .bookAuthor(book.getBookAuthor())
                .bookDescription(book.getBookDescription())
                .bookPrice(book.getBookPrice())
                .discount(discount)
                .bookImage(image)
                .bookQuantity(book.getBookQuantity())
                .build();
    }

    public BookResponseCart mapBookToBookResponseCart(Book book) {
        List<Image> images = imageRepository.findByBook(book);
        List<Discount> discounts = discountRepository.findByBookAndIsActiveTrue(book);
        String image = images.isEmpty() ?
                "https://bableshaazad.com/static/media/bob.a2ca1510718f08b5313d.jpg"
                : images.getFirst().getImage();
        Double discount = discounts.isEmpty() ? 0.0 : discounts.getFirst().getDiscountValue();
        return BookResponseCart.builder()
                .bookid(book.getBookid())
                .bookName(book.getBookName())
                .bookAuthor(book.getBookAuthor())
                .bookDescription(book.getBookDescription())
                .bookPrice(book.getBookPrice())
                .discount(discount)
                .bookImage(image)
                .availabilityStatus(book.getAvailabilityStatus())
                .bookQuantity(book.getBookQuantity())
                .build();
    }
}

