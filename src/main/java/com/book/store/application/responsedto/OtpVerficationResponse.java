package com.book.store.application.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpVerficationResponse {
    private Long userId;
    private String username;
    private boolean success;
}
