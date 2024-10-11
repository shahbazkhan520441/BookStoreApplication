package com.book.store.application.service;

import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import org.springframework.http.ResponseEntity;

public interface WishlistService {
    ResponseEntity<WishlistResponse> addBookToWishlist(WishlistRequest wishlistRequest, Long customerId);
    ResponseEntity<WishlistResponse> removeBookFromWishlist(WishlistRequest wishlistRequest, Long customerId);
}
