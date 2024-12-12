package com.book.store.application.entity;

import com.book.store.application.enums.AvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookid;

    private String bookName;
    private String bookAuthor;
    @Column(length = 5000)
    private String bookDescription;



    private Double bookPrice;
    private Integer bookQuantity;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference // Prevent recursion from the parent side
    private List<Image> images;

}
