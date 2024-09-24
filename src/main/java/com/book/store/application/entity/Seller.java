package com.book.store.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seller extends User {
	

//    @OneToMany
//    private List<Books> books;
}
