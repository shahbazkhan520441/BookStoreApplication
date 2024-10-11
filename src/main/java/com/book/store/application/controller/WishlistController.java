package com.book.store.application.controller;

import com.book.store.application.requestdto.WishlistRequest;
import com.book.store.application.responsedto.WishlistResponse;
import com.book.store.application.service.WishlistService;
import com.book.store.application.util.ErrorStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Wishlist Endpoints", description = "Contains all the endpoints related to managing the customer's wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    //---------------------------------------------------------------------------------------------------

    /**
     * Adds a book to the customer's wishlist.
     *
     * @param wishlistRequest the request body containing details of the book to be added.
     * @param customerId      the ID of the customer to whose wishlist the book is to be added.
     * @return ResponseEntity containing the WishlistResponse object with the updated wishlist.
     */
    @PostMapping("/add/{customerId}")
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
    public ResponseEntity<WishlistResponse> addBookToWishlist(@RequestBody WishlistRequest wishlistRequest, @PathVariable Long customerId) {
        return wishlistService.addBookToWishlist(wishlistRequest, customerId);
    }

    //---------------------------------------------------------------------------------------------------

    /**
     * Removes a book from the customer's wishlist.
     *
     * @param wishlistRequest the request body containing details of the book to be removed.
     * @param customerId      the ID of the customer from whose wishlist the book is to be removed.
     * @return ResponseEntity containing the WishlistResponse object with the updated wishlist.
     */
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
    public ResponseEntity<WishlistResponse> removeBookFromWishlist(@RequestBody WishlistRequest wishlistRequest, @PathVariable Long customerId) {
        return wishlistService.removeBookFromWishlist(wishlistRequest, customerId);
    }
}
