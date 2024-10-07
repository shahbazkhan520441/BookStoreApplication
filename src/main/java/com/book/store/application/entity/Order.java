package com.book.store.application.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    private Book book;
    @ManyToOne
    private Address address;
}