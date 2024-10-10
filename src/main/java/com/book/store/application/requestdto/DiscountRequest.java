package com.book.store.application.requestdto;

import com.book.store.application.enums.DiscountType;
import lombok.Getter;

@Getter
public class DiscountRequest {
    private DiscountType discountType;
    private double discountValue;
    private boolean isActive;
}
