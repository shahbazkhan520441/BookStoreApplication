package com.book.store.application.entity;


import com.book.store.application.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String streetAddress;
    private String streetAddressAdditional;
    private String city;
    private String state;
    private String country;
    private int pincode;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @ManyToOne
    private Customer customer;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Contact> contacts;
   
   
}