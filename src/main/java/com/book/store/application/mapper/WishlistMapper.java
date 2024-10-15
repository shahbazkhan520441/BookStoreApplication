package com.book.store.application.mapper;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Customer;
import com.book.store.application.entity.Wishlist;
import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapper {

    public Wishlist mapWishlistRequestToWishlist(WishlistRequest wishlistRequest, Customer customer, Book book) {
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setBooks(List.of(book)); // Initialize with a single book
        return wishlist;
    }

    public WishlistResponse mapWishlistToWishlistResponse(Wishlist wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setWishlistId(wishlist.getWishlistId());

        // Collect remaining book IDs after removal
        List<Long> remainingBookIds = wishlist.getBooks().stream()
                .map(Book::getBookid) // Extract book IDs
                .collect(Collectors.toList());

        response.setBookIds(remainingBookIds);

        return response;
    }


}
