package com.book.store.application.controller;

import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import com.book.store.application.service.WishlistService;
import com.book.store.application.util.ErrorStructure;
import com.book.store.application.util.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated; // Import for @Validated
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Wishlist Endpoints", description = "Contains all the endpoints related to managing the customer's wishlist")
@Validated // You can also annotate the class with @Validated to validate all methods
public class WishlistController {

    private final WishlistService wishlistService;

    //---------------------------------------------------------------------------------------------------

    @PostMapping("/addWishlist/{customerId}")
    @Operation(summary = "Add Book to Wishlist", description = "Adds a book to the customer's wishlist.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully added to wishlist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WishlistResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<WishlistResponse>> addBookToWishlist(@RequestBody WishlistRequest wishlistRequest, @PathVariable Long customerId) {
        return wishlistService.addBookToWishlist(wishlistRequest, customerId);
    }

    //---------------------------------------------------------------------------------------------------

    @DeleteMapping("/remove/{customerId}")
    @Operation(summary = "Remove Book from Wishlist", description = "Removes a book from the customer's wishlist.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully removed from wishlist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WishlistResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer or book not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<WishlistResponse>> removeBookFromWishlist(@RequestBody WishlistRequest wishlistRequest, @PathVariable Long customerId) {
        return wishlistService.removeBookFromWishlist(wishlistRequest, customerId);
    }

    //---------------------------------------------------------------------------------------------------

    @GetMapping("/wishlist/{customerId}")
    @Operation(summary = "Get Wishlist by Customer ID", description = "Retrieves the wishlist of a customer by customer ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wishlist successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WishlistResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<WishlistResponse>> getWishlistByCustomerId(@PathVariable Long customerId) {
        return wishlistService.getWishlistByCustomerId(customerId);
    }
}
