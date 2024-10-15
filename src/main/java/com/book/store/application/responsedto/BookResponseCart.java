package com.book.store.application.responsedto;

import com.book.store.application.enums.AvailabilityStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseCart {

    private Long bookid;
    private String bookName;
    private String bookAuthor;
    private String bookDescription;
    private String bookImage;
    private Double bookPrice;
    private Double discount;
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
    private Integer bookQuantity;

}
