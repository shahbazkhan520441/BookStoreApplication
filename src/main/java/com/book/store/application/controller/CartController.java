package com.book.store.application.controller;

import com.book.store.application.requestdto.CartRequest;
import com.book.store.application.responsedto.CartBookResponse;
import com.book.store.application.service.CartService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    //    --------------------------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/customers/{customerId}")
    private ResponseEntity<ResponseStructure<CartBookResponse>> addBookInCart(@RequestBody CartRequest cartRequest, @PathVariable Long customerId) {
        System.out.println(cartRequest.getBook().getBookid());
        return cartService.addBookInCart(cartRequest, customerId);
    }

    //    --------------------------------------------------------------------------------------------------------------------------------------------------------------

    @PutMapping("/customers/cart-books/{cartId}")
    private ResponseEntity<ResponseStructure<CartBookResponse>> updateCart(
            @RequestParam int selectedQuantity,
            @PathVariable Long cartId) {
        return cartService.updateCart(selectedQuantity, cartId);
    }

    //    --------------------------------------------------------------------------------------------------------------------------------------------------------------

    @DeleteMapping("/customers/{customerId}/cart-books/{cartId}")
    private ResponseEntity<ResponseStructure<CartBookResponse>> removeCartProduct(
            @PathVariable Long customerId,
            @PathVariable Long cartId) {
        return cartService.removeCartProduct(customerId, cartId);
    }

    //    --------------------------------------------------------------------------------------------------------------------------------------------------------------


    @DeleteMapping("/customers/{customerId}/cart-books")
    private ResponseEntity<ResponseStructure<String>> removeAllCartProduct(
            @PathVariable Long customerId) {
        return cartService.removeAllCartProduct(customerId);
    }

    //    --------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/customers/{customerId}/cart-books")
    private ResponseEntity<ResponseStructure<List<CartBookResponse>>> getCartProducts(
            @PathVariable Long customerId) {
        return cartService.getCartProducts(customerId);
    }

    //    --------------------------------------------------------------------------------------------------------------------------------------------------------------


}
