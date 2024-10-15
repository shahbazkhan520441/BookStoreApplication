package com.book.store.application.controller;

import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.responsedto.OrderResponseDto;
import com.book.store.application.service.OrderService;
import com.book.store.application.util.ResponseStructure;
import com.book.store.application.util.ErrorStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;  // Import for @Valid
import java.util.List;

/**
 * Controller class for managing purchase orders, including creating, retrieving,
 * and listing customer purchase orders.
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Order Endpoints", description = "Endpoints for managing purchase orders.")
public class OrderController {

    private final OrderService orderService;

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Generates a new purchase order for a customer based on the provided book, customer, and address.
     *
     * @param orderRequest the request containing order details such as quantity, payment details, etc.
     * @param bookId       the ID of the book to be ordered.
     * @param customerId   the ID of the customer placing the order.
     * @param addressId    the ID of the address where the book will be delivered.
     * @return a response containing the created purchase order details.
     */
    @PostMapping("/customers/{customerId}/addresses/{addressId}/books/{bookId}/purchase-orders")
    @Operation(summary = "Create Purchase Order", description = "Generates a new purchase order for the customer.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Customer, address, or book not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(
            @RequestBody @Valid OrderRequest orderRequest,  // Added @Valid annotation here
            @PathVariable @Valid Long bookId,               // Added @Valid annotation here
            @PathVariable @Valid Long customerId,           // Added @Valid annotation here
            @PathVariable @Valid Long addressId) {          // Added @Valid annotation here
        return orderService.generatePurchaseOrder(orderRequest, bookId, customerId, addressId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a list of all purchase orders for a specific customer.
     *
     * @param customerId the ID of the customer whose orders are to be retrieved.
     * @return a response containing the list of purchase orders.
     */
    @GetMapping("/customers/{customerId}/purchase-orders")
    @Operation(summary = "List Purchase Orders", description = "Retrieves all purchase orders for a customer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders successfully retrieved", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Customer or orders not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<OrderResponseDto>>> findPurchaseOrders(
            @PathVariable @Valid Long customerId) {  // Added @Valid annotation here
        return orderService.findPurchaseOrders(customerId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves details of a specific purchase order based on the order ID.
     *
     * @param orderId the ID of the purchase order to be retrieved.
     * @return a response containing the purchase order details.
     */
    @GetMapping("/customers/purchaseOrders/{orderId}")
    @Operation(summary = "Get Purchase Order", description = "Retrieves details of a specific purchase order.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully retrieved", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<OrderResponseDto>> findPurchaseOrder(@PathVariable @Valid Long orderId) {  // Added @Valid annotation here
        return orderService.findPurchaseOrder(orderId);
    }

    //------------------------------------------------------------------------------------------------------------------------
}
