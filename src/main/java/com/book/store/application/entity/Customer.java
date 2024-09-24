package com.book.store.application.entity;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends User {

    

//    @OneToMany
//    private List<Order> orders;
//
//    @OneToMany
//    private List<CartProduct> cartProducts;
//
//    @OneToOne
//    private Wishlist wishlist;

}