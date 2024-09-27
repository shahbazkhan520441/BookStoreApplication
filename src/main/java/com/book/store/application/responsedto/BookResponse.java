package com.book.store.application.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String bookName;
    private String bookAuthor;
    private String bookDescription;
    private byte[] bookLogo;
    private Double bookPrice;
    private Integer bookQuantity;
}
