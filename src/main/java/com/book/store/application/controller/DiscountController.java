package com.book.store.application.controller;

import com.book.store.application.requestdto.DiscountRequest;
import com.book.store.application.responsedto.DiscountResponse;
import com.book.store.application.service.DiscountService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class DiscountController {

    private DiscountService discountService;

    //---------------------------------------------------------------------------------------------------
    @GetMapping("/products/{productId}/discounts")
    public ResponseEntity<ResponseStructure<List<DiscountResponse>>> getDiscounts(
            @PathVariable Long productId) {
        return discountService.getDiscounts(productId);
    }
    //---------------------------------------------------------------------------------------------------

    @PutMapping("/sellers/{sellerId}/discounts/{discountId}")
    public ResponseEntity<ResponseStructure<DiscountResponse>> updateDiscount(
            @PathVariable Long sellerId,
            @PathVariable Long discountId,
             @RequestBody DiscountRequest discountRequest) {
        return discountService.updateDiscount(sellerId, discountId, discountRequest);
    }
}