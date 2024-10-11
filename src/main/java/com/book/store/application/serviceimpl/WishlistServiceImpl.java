package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Wishlist;
import com.book.store.application.exception.BookNotExistException;
import com.book.store.application.exception.CustomerNotExistException;
import com.book.store.application.mapper.WishlistMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.CustomerRepository;
import com.book.store.application.repository.WishlistRepository;
import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import com.book.store.application.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    public ResponseEntity<WishlistResponse> addBookToWishlist(WishlistRequest wishlistRequest, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id : " + customerId + ", does not exist"));

        Book book = bookRepository.findById(wishlistRequest.getBookId())
                .orElseThrow(() -> new BookNotExistException("Book Id : " + wishlistRequest.getBookId() + ", does not exist"));

        // Check if customer already has a wishlist
        Wishlist wishlist;
        if (customer.getWishlists() == null || customer.getWishlists().isEmpty()) {
            // Create a new wishlist
            wishlist = wishlistMapper.mapWishlistRequestToWishlist(wishlistRequest, customer, book);
        } else {
            // Add the book to the existing wishlist
            wishlist = customer.getWishlists().get(0); // Get the first wishlist
            wishlist.getBooks().add(book);
        }

        // Save the wishlist
        wishlistRepository.save(wishlist);

        WishlistResponse responseDto = wishlistMapper.mapWishlistToWishlistResponseDto(wishlist);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    public ResponseEntity<WishlistResponse> removeBookFromWishlist(WishlistRequest wishlistRequest, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id : " + customerId + ", does not exist"));

        Wishlist wishlist = customer.getWishlists().get(0); // Get the first wishlist

        // Remove the book from the wishlist
        Optional<Book> bookToRemove = wishlist.getBooks().stream()
                .filter(book -> book.getBookid().equals(wishlistRequest.getBookId()))
                .findFirst();

        if (bookToRemove.isPresent()) {
            wishlist.getBooks().remove(bookToRemove.get());
            wishlistRepository.save(wishlist); // Save updated wishlist
        }

        WishlistResponse responseDto = wishlistMapper.mapWishlistToWishlistResponseDto(wishlist);
        return ResponseEntity.ok(responseDto);
    }
}
