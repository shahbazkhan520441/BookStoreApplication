package com.book.store.application.controller;

import com.book.store.application.requestdto.DiscountRequest;
import com.book.store.application.responsedto.DiscountResponse;
import com.book.store.application.service.DiscountService;
import com.book.store.application.util.ErrorStructure;
import com.book.store.application.util.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Discount Endpoints", description = "Contains all the endpoints related to discount management in the bookstore application")
public class DiscountController {

    private final DiscountService discountService;

    //---------------------------------------------------------------------------------------------------

    /**
     * Retrieves a list of discounts applicable to a specific product by its ID.
     *
     * @param productId the ID of the product for which discounts are to be retrieved.
     * @return ResponseEntity containing a ResponseStructure with a list of DiscountResponse objects for the specified product.
     */
    @GetMapping("/products/{productId}/discounts")
    @Operation(summary = "Get Discounts for a Product", description = "Retrieves a list of discounts applicable to a specific product.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Discounts successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found or no discounts available", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<DiscountResponse>>> getDiscounts(
            @PathVariable Long productId) {
        return discountService.getDiscounts(productId);
    }

    //---------------------------------------------------------------------------------------------------

    /**
     * Updates an existing discount for a specific seller.
     *
     * @param sellerId      the ID of the seller whose discount is to be updated.
     * @param discountId    the ID of the discount to be updated.
     * @param discountRequest the request body containing the new discount details.
     * @return ResponseEntity containing a ResponseStructure with the updated DiscountResponse object.
     */
    @PutMapping("/sellers/{sellerId}/discounts/{discountId}")
    @Operation(summary = "Update Discount", description = "Updates an existing discount for a specific seller.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Discount successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Discount or seller not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<DiscountResponse>> updateDiscount(
            @PathVariable Long sellerId,
            @PathVariable Long discountId,
            @RequestBody DiscountRequest discountRequest) {
        return discountService.updateDiscount(sellerId, discountId, discountRequest);
    }
}
