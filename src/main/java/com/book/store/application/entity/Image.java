package com.book.store.application.entity;

import com.book.store.application.enums.ImageType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String image;
    @Enumerated(EnumType.STRING)
    private ImageType imageType;
    @ManyToOne
    @JsonBackReference // Prevents recursion on the back side
    private Book book;
}