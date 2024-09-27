package com.book.store.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookid;

    private String bookName;
    private String bookAuthor;
    private String bookDescription;

    @Lob
    private byte[] bookLogo;

    private Double bookPrice;
    private Integer bookQuantity;

}
