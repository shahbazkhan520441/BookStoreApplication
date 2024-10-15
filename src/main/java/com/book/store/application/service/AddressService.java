package com.book.store.application.service;

import com.book.store.application.requestdto.AddressRequest;
import com.book.store.application.responsedto.AddressResponse;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {
    ResponseEntity<ResponseStructure<AddressResponse>> addAddress(AddressRequest addressRequest, Long userId);

    ResponseEntity<ResponseStructure<List<AddressResponse>>> getAddress(Long userId);

    ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(Long addressId, AddressRequest addressRequest);
}