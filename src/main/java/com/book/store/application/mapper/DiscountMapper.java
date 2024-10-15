package com.book.store.application.mapper;

import com.book.store.application.entity.Discount;
import com.book.store.application.requestdto.DiscountRequest;
import com.book.store.application.responsedto.DiscountResponse;
import org.springframework.stereotype.Component;

@Component

public class DiscountMapper {

    public DiscountResponse mapDiscountToDiscountResponse(Discount discount) {
        return DiscountResponse.builder()
                .discountId(discount.getDiscountId())
                .discountType(discount.getDiscountType())
                .discountValue(discount.getDiscountValue())
                .isActive(discount.getIsActive())
                .build();
    }

    public Discount mapDiscountRequestToDiscount(DiscountRequest
                                                         discountRequest, Discount discount) {
        discount.setDiscountValue(discountRequest.getDiscountValue());
        discount.setDiscountType(discountRequest.getDiscountType());
        discount.setActive(discount.getIsActive());
        return discount;
    }
}