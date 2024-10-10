package com.book.store.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @ManyToOne
    private Book book;
    @ManyToOne
    private Address address;
}