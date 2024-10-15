package com.book.store.application.responsedto;

import com.book.store.application.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountResponse {
    private Long discountId;
    private DiscountType discountType;
    private double discountValue;
    private boolean isActive;
}