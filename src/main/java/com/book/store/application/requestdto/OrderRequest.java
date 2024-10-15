package com.book.store.application.requestdto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    @Min(value = 1, message = "Total quantity must be at least 1")
    private int totalQuantity;

    @Min(value = 0, message = "Total price must be at least 0")
    private double totalPrice;

    @Min(value = 0, message = "Discount must be at least 0")
    private double discount;

    @Min(value = 0, message = "Discount price must be at least 0")
    private double discountPrice;

    @Min(value = 0, message = "Total payable amount must be at least 0")
    private double totalPayableAmount;
}
