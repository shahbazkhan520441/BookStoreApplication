package com.book.store.application.controller;

import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.CustomerService;
import com.book.store.application.util.ResponseStructure;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    //------------------------------------------------------------------------------------------------------------------------
//    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<ResponseStructure<UserResponse>> findCustomer(@Valid @PathVariable Long customerId) {
        return customerService.findCustomer(customerId);
    }
    //------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/customers")
//    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers() {
        return customerService.findCustomers();
    }
}