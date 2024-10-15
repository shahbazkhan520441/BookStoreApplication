
package com.book.store.application.requestdto;

import com.book.store.application.entity.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {

    @Min(value = 1, message = "Selected quantity must be at least 1")
    private int selectedQuantity;

    @NotNull(message = "Book cannot be null")
    private Book book;
}
