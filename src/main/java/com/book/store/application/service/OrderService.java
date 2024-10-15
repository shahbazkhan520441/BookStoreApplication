package com.book.store.application.service;

import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.responsedto.OrderResponseDto;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(OrderRequest orderRequest, Long bookId, Long customerId, Long addressId);

    public ResponseEntity<ResponseStructure<List<OrderResponseDto>>> findPurchaseOrders(Long customerId);


    ResponseEntity<ResponseStructure<OrderResponseDto>> findPurchaseOrder(Long orderId);
}
