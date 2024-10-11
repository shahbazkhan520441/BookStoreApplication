package com.book.store.application.controller;

import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import com.book.store.application.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add/{customerId}")
    public ResponseEntity<WishlistResponse> addBookToWishlist(@RequestBody WishlistRequest wishlistRequest, @PathVariable Long customerId) {
        return wishlistService.addBookToWishlist(wishlistRequest, customerId);
    }

    @DeleteMapping("/remove/{customerId}")
    public ResponseEntity<WishlistResponse> removeBookFromWishlist(@RequestBody WishlistRequest wishlistRequest, @PathVariable Long customerId) {
        return wishlistService.removeBookFromWishlist(wishlistRequest, customerId);
    }
}
