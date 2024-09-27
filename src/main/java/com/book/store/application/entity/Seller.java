package com.book.store.application.entity;

import com.book.store.application.enums.UserRole;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seller extends User {

//    @OneToMany
//    private List<Books> books;
}
