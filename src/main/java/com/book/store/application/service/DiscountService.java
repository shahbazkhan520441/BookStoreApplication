package com.book.store.application.service;

import com.book.store.application.requestdto.DiscountRequest;
import com.book.store.application.responsedto.DiscountResponse;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiscountService {
    ResponseEntity<ResponseStructure<List<DiscountResponse>>> getDiscounts(Long productId);

    ResponseEntity<ResponseStructure<DiscountResponse>> updateDiscount(Long sellerId, Long discountId, DiscountRequest discountRequest);
}
