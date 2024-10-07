package com.book.store.application.service;

import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity<ResponseStructure<UserResponse>> findCustomer(Long customerId);

    ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers();
//    Demo commit add for testing
}