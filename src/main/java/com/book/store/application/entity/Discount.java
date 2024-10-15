package com.book.store.application.entity;



import com.book.store.application.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private double discountValue;
    private boolean isActive;
    @ManyToOne
    private Book book;

    public boolean getIsActive() {
        return this.isActive;
    }

}