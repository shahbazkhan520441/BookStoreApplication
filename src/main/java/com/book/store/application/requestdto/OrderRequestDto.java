package com.book.store.application.requestdto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {

    private Long orderId;
    private Long customerId;
    private Integer totalQuantity;
    private Double totalPrice;
    private Double discount;
    private Double discountPrice;
    private Double totalPayableAmount;
    private AddressDto addressDto;
}
