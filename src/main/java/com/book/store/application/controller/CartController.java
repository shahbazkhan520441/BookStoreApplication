package com.book.store.application.controller;

import com.book.store.application.requestdto.CartRequest;
import com.book.store.application.responsedto.CartBookResponse;
import com.book.store.application.service.CartService;
import com.book.store.application.util.ResponseStructure;
import com.book.store.application.util.ErrorStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing customer carts, including adding books to the cart,
 * updating cart items, removing items, and retrieving cart contents.
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Cart Endpoints", description = "Endpoints for managing customer carts.")
public class CartController {

    private final CartService cartService;

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds a book to the customer's cart.
     *
     * @param cartRequest the request containing book details to add to the cart.
     * @param customerId  the ID of the customer adding the book.
     * @return a response containing the cart book details.
     */
    @PostMapping("/customers/{customerId}")
    @Operation(summary = "Add a Book to Cart", description = "Adds a book to the customer's cart based on the given customer ID.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book successfully added to cart", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartBookResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Customer or book not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<CartBookResponse>> addBookInCart(
            @Valid @RequestBody CartRequest cartRequest, // Added @Valid here
            @PathVariable Long customerId) {
        return cartService.addBookInCart(cartRequest, customerId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Updates the quantity of a selected book in the cart.
     *
     * @param selectedQuantity the new quantity of the book.
     * @param cartId           the ID of the cart item to update.
     * @return a response containing the updated cart book details.
     */
    @PutMapping("/customers/cart-books/{cartId}")
    @Operation(summary = "Update Cart Quantity", description = "Updates the quantity of a selected book in the cart based on the cart ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart quantity successfully updated", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartBookResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Cart not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<CartBookResponse>> updateCart(
            @RequestParam int selectedQuantity,
            @PathVariable Long cartId) {
        return cartService.updateCart(selectedQuantity, cartId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Removes a specific product from the customer's cart.
     *
     * @param customerId the ID of the customer.
     * @param cartId     the ID of the cart item to remove.
     * @return a response confirming the removal of the product.
     */
    @DeleteMapping("/customers/{customerId}/cart-books/{cartId}")
    @Operation(summary = "Remove Product from Cart", description = "Removes a specific product from the customer's cart based on the cart ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully removed from the cart", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartBookResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Cart or product not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<CartBookResponse>> removeCartProduct(
            @PathVariable Long customerId,
            @PathVariable Long cartId) {
        return cartService.removeCartProduct(customerId, cartId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Removes all products from the customer's cart.
     *
     * @param customerId the ID of the customer.
     * @return a response confirming the removal of all products.
     */
    @DeleteMapping("/customers/{customerId}/cart-books")
    @Operation(summary = "Remove All Products from Cart", description = "Removes all products from the customer's cart based on the customer ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All products successfully removed from the cart", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Customer or cart not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<String>> removeAllCartProduct(
            @PathVariable Long customerId) {
        return cartService.removeAllCartProduct(customerId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves all products in the customer's cart.
     *
     * @param customerId the ID of the customer.
     * @return a response containing a list of cart products.
     */
    @GetMapping("/customers/{customerId}/cart-books")
    @Operation(summary = "Get Cart Products", description = "Retrieves all products in the customer's cart based on the customer ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products successfully retrieved", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CartBookResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Customer or cart not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<CartBookResponse>>> getCartProducts(
            @PathVariable Long customerId) {
        return cartService.getCartProducts(customerId);
    }

    //------------------------------------------------------------------------------------------------------------------------

}
