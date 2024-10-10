package com.book.store.application.requestdto;

import com.book.store.application.enums.AddressType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private AddressType addressType;
    private String streetAddress;
    private String streetAddressAdditional;
    private String city;
    private String state;
    private String country;
    private int pincode;

    private String contactNumber1;
    private String contactNumber2;
}