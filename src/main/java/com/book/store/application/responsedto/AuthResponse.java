package com.book.store.application.responsedto;

import com.book.store.application.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private Long userId;
    private String username;
    private UserRole userRole;
    private long accessExpiration;
    private long refreshExpiration;

}
