package com.book.store.application.controller;

import com.book.store.application.requestdto.AddressRequest;
import com.book.store.application.responsedto.AddressResponse;
import com.book.store.application.service.AddressService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(
            @RequestBody AddressRequest addressRequest,
            @PathVariable Long userId) {
        return addressService.addAddress(addressRequest, userId);
    }

    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<ResponseStructure<List<AddressResponse>>> getAddress(@PathVariable Long userId) {
        return addressService.getAddress(userId);
    }

    @PutMapping("/users/addresses/{addressId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequest addressRequest) {
        return addressService.updateAddress(addressId, addressRequest);
    }


}