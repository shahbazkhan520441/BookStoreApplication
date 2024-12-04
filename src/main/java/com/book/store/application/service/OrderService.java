package com.book.store.application.service;

import com.book.store.application.requestdto.OrderRequest;
import com.book.store.application.responsedto.OrderResponseDto;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    public ResponseEntity<ResponseStructure<List<OrderResponseDto>>> findPurchaseOrders(Long customerId);


    ResponseEntity<ResponseStructure<OrderResponseDto>> findPurchaseOrder(Long orderId);
    ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(OrderRequest orderRequest, Long customerId, Long addressId);
}
