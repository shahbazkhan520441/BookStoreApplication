package com.book.store.application.requestdto;

import com.book.store.application.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    private int selectedQuantity;
    private Book book;
}
