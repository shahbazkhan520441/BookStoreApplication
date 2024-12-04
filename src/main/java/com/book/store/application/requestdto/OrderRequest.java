package com.book.store.application.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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

    @NotNull(message = "Cart IDs cannot be null")
    @NotEmpty(message = "At least one cart ID must be provided")
    private List<Long> cartIds;
}
