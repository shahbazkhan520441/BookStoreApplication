package com.book.store.application.responsedto;


import com.book.store.application.requestdto.AddressDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private  Long customerId;
    private List<Long> bookIds;
    private Integer totalQuantity;
    private Double totalPrice;
    private Double discount;
    private Double discountPrice;
    private Double totalPayableAmount;
    private AddressDto addressDto;
}
