package com.book.store.application.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

    @OneToMany
    private List<Order> orders;

    @OneToMany
    private List<Cart> carts;

//    @OneToOne
//    private Wishlist wishlist;


}