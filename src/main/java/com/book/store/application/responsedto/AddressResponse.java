package com.book.store.application.responsedto;

import com.book.store.application.entity.Contact;
import com.book.store.application.enums.AddressType;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressResponse {
    private Long addressId;
    private String streetAddress;
    private String streetAddressAdditional;
    private String city;
    private String state;
    private String country;
    private int pincode;
    private AddressType addressType;
    private List<Contact> contacts;
}