package com.book.store.application.service;

import com.book.store.application.requestdto.CartRequest;
import com.book.store.application.responsedto.CartBookResponse;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    ResponseEntity<ResponseStructure<CartBookResponse>> addBookInCart(CartRequest cartRequest, Long customerId);
    public ResponseEntity<ResponseStructure<CartBookResponse>> updateCart(
            int selectedQuantity, Long cartId);

    ResponseEntity<ResponseStructure<CartBookResponse>> removeCartProduct(Long customerId, Long cartsId);

    ResponseEntity<ResponseStructure<String>> removeAllCartProduct(Long customerId);

    ResponseEntity<ResponseStructure<List<CartBookResponse>>> getCartProducts(Long customerId);
}
