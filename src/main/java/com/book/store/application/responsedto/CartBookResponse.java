package com.book.store.application.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartBookResponse {
    private Long cartId;
    private int selectedQuantity;
    private BookResponseCart book;
}