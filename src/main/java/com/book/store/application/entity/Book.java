package com.book.store.application.entity;

import com.book.store.application.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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



    private Double bookPrice;
    private Integer bookQuantity;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    @OneToMany(mappedBy = "book")
    private List<Image> images;

}
