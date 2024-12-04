package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Image;
import com.book.store.application.entity.Wishlist;
import com.book.store.application.exception.BookNotExistException;
import com.book.store.application.exception.CustomerNotExistException;
import com.book.store.application.exception.WishlistNotFoundException;
import com.book.store.application.mapper.BookMapper;
import com.book.store.application.mapper.WishlistMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.CustomerRepository;
import com.book.store.application.repository.WishlistRepository;
import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.responsedto.WishlistResponse;
import com.book.store.application.service.WishlistService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final BookMapper bookMapper;

    @Override
    public ResponseEntity<ResponseStructure<WishlistResponse>> addBookToWishlist(WishlistRequest wishlistRequest, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer with ID: " + customerId + " does not exist"));

        Book book = bookRepository.findById(wishlistRequest.getBookId())
                .orElseThrow(() -> new BookNotExistException("Book with ID: " + wishlistRequest.getBookId() + " does not exist"));

        Wishlist wishlist;
        ResponseStructure<WishlistResponse> responseStructure = new ResponseStructure<>();
        if (customer.getWishlists() == null || customer.getWishlists().isEmpty()) {
            // Create a new wishlist
            wishlist = wishlistMapper.mapWishlistRequestToWishlist(wishlistRequest, customer, book);
            wishlistRepository.save(wishlist);
            WishlistResponse responseDto = wishlistMapper.mapWishlistToWishlistResponse(wishlist);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    responseStructure
                            .setStatus(HttpStatus.CREATED.value())
                            .setMessage("Wishlist created and book added successfully.")
                            .setData(responseDto)
            );
        } else {
            wishlist = customer.getWishlists().get(0); // Get the first wishlist
            if (wishlist.getBooks().contains(book)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        responseStructure
                                .setStatus(HttpStatus.CONFLICT.value())
                                .setMessage("Book is already in your wishlist.")
                                .setData(null)
                );
            }
            wishlist.getBooks().add(book);
            wishlistRepository.save(wishlist);
            WishlistResponse responseDto = wishlistMapper.mapWishlistToWishlistResponse(wishlist);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    responseStructure
                            .setStatus(HttpStatus.CREATED.value())
                            .setMessage("Book added to wishlist successfully.")
                            .setData(responseDto)
            );
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<WishlistResponse>> removeBookFromWishlist(WishlistRequest wishlistRequest, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer with ID: " + customerId + " does not exist"));

        Wishlist wishlist = customer.getWishlists().get(0); // Assuming the first wishlist is being used.

        // Find the book to be removed by its ID from the wishlist
        Optional<Book> bookToRemove = wishlist.getBooks().stream()
                .filter(book -> book.getBookid().equals(wishlistRequest.getBookId()))
                .findFirst();

        ResponseStructure<WishlistResponse> responseStructure = new ResponseStructure<>();

        if (bookToRemove.isPresent()) {
            // Capture the book ID to remove
            Long removedBookId = bookToRemove.get().getBookid();

            // Remove the book from the wishlist
            wishlist.getBooks().remove(bookToRemove.get());

            // Save the updated wishlist to persist changes
            wishlistRepository.save(wishlist);

            // Map the updated wishlist to the response DTO
            WishlistResponse responseDto = wishlistMapper.mapWishlistToWishlistResponse(wishlist);

            // Return response with the correct removed book ID and remaining books
            return ResponseEntity.ok(
                    responseStructure
                            .setStatus(HttpStatus.OK.value())
                            .setMessage("Book removed from wishlist successfully. Removed book ID: " + removedBookId)
                            .setData(responseDto)
            );
        } else {
            // If book is not found in the wishlist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    responseStructure
                            .setStatus(HttpStatus.NOT_FOUND.value())
                            .setMessage("Book not found in your wishlist.")
                            .setData(null)
            );
        }
    }





    @Override
    public ResponseEntity<ResponseStructure<WishlistResponse>> getWishlistByCustomerId(Long customerId) {
        // Fetch customer and validate existence
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer with ID: " + customerId + " does not exist"));

        // Fetch the first wishlist for the customer
        Wishlist wishlist = customer.getWishlists().get(0); // Ensure this logic aligns with your business rules

        // Map books in the wishlist to BookResponse using the BookMapper
        List<BookResponse> bookResponses = wishlist.getBooks().stream()
                .map(book -> bookMapper.mapBookToBookResponse(book)) // Use the improved mapper
                .collect(Collectors.toList());

        // Create WishlistResponse DTO
        WishlistResponse wishlistResponse = new WishlistResponse();
        wishlistResponse.setWishlistId(wishlist.getWishlistId());
        wishlistResponse.setBooks(bookResponses);

        // Create ResponseStructure and return
        ResponseStructure<WishlistResponse> responseStructure = new ResponseStructure<>();
        responseStructure.setStatus(HttpStatus.OK.value())
                .setMessage("Wishlist retrieved successfully.")
                .setData(wishlistResponse);

        return ResponseEntity.ok(responseStructure);
    }




}
