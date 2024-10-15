package com.book.store.application.requestdto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {

    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @Min(value = 1, message = "Total quantity must be at least 1")
    private Integer totalQuantity;

    @Min(value = 0, message = "Total price must be at least 0")
    private Double totalPrice;

    @Min(value = 0, message = "Discount must be at least 0")
    private Double discount;

    @Min(value = 0, message = "Discounted price must be at least 0")
    private Double discountPrice;

    @Min(value = 0, message = "Total payable amount must be at least 0")
    private Double totalPayableAmount;

    @NotNull(message = "Address details are required")
    @Valid
    private AddressDto addressDto;
}
