
package com.book.store.application.requestdto;

import com.book.store.application.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookRequest {

    @NotBlank(message = "Book name cannot be blank")
    @Size(max = 100, message = "Book name must not exceed 100 characters")
    private String bookName;

    @NotBlank(message = "Book author cannot be blank")
    @Size(max = 50, message = "Book author must not exceed 50 characters")
    private String bookAuthor;

    @Size(max = 500, message = "Book description must not exceed 500 characters")
    private String bookDescription;

    @NotNull(message = "Book price cannot be null")
    @Min(value = 0, message = "Book price must be at least 0")
    private Double bookPrice;

    @NotNull(message = "Discount type is required")
    private DiscountType discountType;

    @Max(value = 99, message = "Discount value must not exceed 99%")
    @Min(value = 0, message = "Discount value must be at least 0%")
    private double discount;
}
