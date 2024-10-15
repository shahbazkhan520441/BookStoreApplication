package com.book.store.application.service;

import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

public interface WishlistService {
     ResponseEntity<ResponseStructure<WishlistResponse>> addBookToWishlist(WishlistRequest wishlistRequest, Long customerId);
     ResponseEntity<ResponseStructure<WishlistResponse>> removeBookFromWishlist(WishlistRequest wishlistRequest, Long customerId) ;

    ResponseEntity<ResponseStructure<WishlistResponse>> getWishlistByCustomerId(Long customerId);
}
