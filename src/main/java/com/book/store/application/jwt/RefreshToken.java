package com.book.store.application.jwt;

import com.book.store.application.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;
    private String refreshToken;
    private LocalDateTime expiration;
    private boolean isBlocked;

    @ManyToOne
    private User user;
}