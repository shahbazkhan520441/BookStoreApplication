package com.book.store.application.controller;

import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.responsedto.OrderResponseDto;
import com.book.store.application.service.OrderService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    //---------------------------------------------------------------------------------------------------
    @PostMapping("/customers/{customerId}/addresses/{addressId}/books/{bookId}/purchase-orders")
    public ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(
            @RequestBody OrderRequest orderRequest,
            @PathVariable Long bookId,
            @PathVariable Long customerId,
            @PathVariable Long addressId) {
        return orderService.generatePurchaseOrder(orderRequest, bookId, customerId, addressId);
    }

    //    -----------------------------------------------------------------------------------------------------------------
    @GetMapping("/customers/{customerId}/purchase-orders")
    public ResponseEntity<ResponseStructure<List<OrderResponseDto>>> findPurchaseOrders(
            @PathVariable Long customerId) {
        return orderService.findPurchaseOrders(customerId);
    }

    //    -----------------------------------------------------------------------------------------------------------------

    @GetMapping("/customers/purchaseOrders/{orderId}")
    public ResponseEntity<ResponseStructure<OrderResponseDto>> findPurchaseOrder(@PathVariable Long orderId) {
        return orderService.findPurchaseOrder(orderId);
    }

}
