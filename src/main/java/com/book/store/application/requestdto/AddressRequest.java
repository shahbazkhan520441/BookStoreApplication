package com.book.store.application.requestdto;

import com.book.store.application.enums.AddressType;
import lombok.Getter;

@Getter
public class AddressRequest {
    private String streetAddress;
    private String streetAddressAdditional;
    private String city;
    private String state;
    private String country;
    private int pincode;
    private AddressType addressType;
}