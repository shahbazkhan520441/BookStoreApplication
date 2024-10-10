package com.book.store.application.requestdto;

import com.book.store.application.enums.DiscountType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@Builder
public class BookRequest {

    private String bookName;
    private String bookAuthor;
    private String bookDescription;
    private Double bookPrice;

    @NotNull(message = "Discount type is required")
    private DiscountType discountType;

    @Max(value = 99, message = "Discount value must not exceed 99%")
    @Min(value = 0, message = "Discount value must be at least 0%")
    private double discount;





}
