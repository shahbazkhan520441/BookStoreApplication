
package com.book.store.application.requestdto;

import com.book.store.application.enums.DiscountType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DiscountRequest {

    @NotNull(message = "Discount type cannot be null")
    private DiscountType discountType;

    @Min(value = 0, message = "Discount value must be at least 0%")
    @Max(value = 99, message = "Discount value must not exceed 99%")
    private double discountValue;

    private boolean isActive;
}
