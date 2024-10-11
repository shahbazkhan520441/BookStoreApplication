package com.book.store.application.requestdto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistRequest {
    private Long wishlistId;
    private Long bookId;
}
