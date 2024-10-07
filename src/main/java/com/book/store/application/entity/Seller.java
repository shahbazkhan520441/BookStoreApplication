package com.book.store.application.entity;

import com.book.store.application.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Seller extends User {

//    @OneToMany
//    private List<Book> books;
}
