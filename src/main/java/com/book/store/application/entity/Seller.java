package com.book.store.application.entity;

import com.book.store.application.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Seller extends User {

    @OneToOne
    private Address address;

    @OneToMany
    private List<Book> books;
}
