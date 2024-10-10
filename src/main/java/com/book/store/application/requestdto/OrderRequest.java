package com.book.store.application.requestdto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {;
    private int totalQuantity;
    private double totalPrice;
    private double discount;
    private double discountPrice;
    private double totalPayableAmount;
}