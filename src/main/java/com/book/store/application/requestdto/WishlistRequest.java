package com.book.store.application.requestdto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistRequest {

//    @NotNull(message = "Wishlist ID cannot be null")
//    private Long wishlistId;

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;
}
