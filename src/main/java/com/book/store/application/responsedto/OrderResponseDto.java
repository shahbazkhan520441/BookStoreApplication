package com.book.store.application.responsedto;


import com.book.store.application.requestdto.AddressDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private  Long customerId;
    private Long bookId;
    private Integer totalQuantity;
    private Double totalPrice;
    private Double discount;
    private Double discountPrice;
    private Double totalPayableAmount;
    private AddressDto addressDto;
}
