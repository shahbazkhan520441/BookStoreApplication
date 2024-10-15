package com.book.store.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToMany
    private List<Book> books;

    @ManyToOne // Changed to ManyToOne for multiple wishlists
    private Customer customer;
}
