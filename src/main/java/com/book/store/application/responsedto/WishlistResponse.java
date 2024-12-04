package com.book.store.application.responsedto;

import com.book.store.application.entity.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;



@Getter
@Setter
public class WishlistResponse {
    private Long wishlistId;
    private List<BookResponse> books; // List of book IDs in the wishlist
}

