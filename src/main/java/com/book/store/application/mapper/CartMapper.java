package com.book.store.application.mapper;

import com.book.store.application.entity.Cart;
import com.book.store.application.requestdto.CartRequest;
import com.book.store.application.responsedto.CartBookResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartMapper {
    private final BookMapper bookMapper;

    public Cart mapCartRequestToCart(CartRequest cartRequest, Cart cart) {
        cart.setSelectedQuantity(cartRequest.getSelectedQuantity());
        cart.setBook(cartRequest.getBook());
        return cart;
    }

    public CartBookResponse mapCartToCartResponse(Cart cart) {
        System.out.println(cart.getBook()+"------------------------------------------------------------------------");
        return CartBookResponse.builder()
                .cartId(cart.getCartId())
                .selectedQuantity(cart.getSelectedQuantity())
                .book(bookMapper.mapBookToBookResponseCart(cart.getBook()))
                .build();
    }




}
