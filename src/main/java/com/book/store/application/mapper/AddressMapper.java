package com.book.store.application.mapper;

import com.book.store.application.entity.Address;
import com.book.store.application.enums.AddressType;
import com.book.store.application.requestdto.AddressRequest;
import com.book.store.application.responsedto.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address mapAddressRequestToAddress(AddressRequest addressRequest, Address address) {
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setStreetAddressAdditional(addressRequest.getStreetAddressAdditional());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setCountry(addressRequest.getCountry());
        address.setPincode(addressRequest.getPincode());
        address.setAddressType(addressRequest.getAddressType());
        return address;
    }

    public AddressResponse mapAddressToAddressResponse(Address address) {
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .streetAddress(address.getStreetAddress())
                .streetAddressAdditional(address.getStreetAddressAdditional())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .addressType(address.getAddressType())
                .contacts(address.getContacts())
                .build();
    }

}