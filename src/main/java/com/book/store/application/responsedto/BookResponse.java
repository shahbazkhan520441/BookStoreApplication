package com.book.store.application.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long bookid;
    private String bookName;
    private String bookAuthor;
    private String bookDescription;
    private String bookImage;
    private Double bookPrice;
    private Double discount;
    private Integer bookQuantity;
}
