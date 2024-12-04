package com.book.store.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long orderId;
    private int totalQuantity;
    private double totalPrice;
    private double discount;
    private double discountPrice;

    private double totalPayableAmount;
    private LocalDate orderDate;

    @ManyToOne
    private Customer customer;
    @ManyToMany
    private List<Book> books;
    @ManyToOne
    private Address address;
}