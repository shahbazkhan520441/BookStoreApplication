package com.book.store.application.responsedto;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import com.book.store.application.enums.UserRole;

@Getter
@Setter
public class UserResponse {

    private Long userId; 
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate dob;
    private String email;
    private UserRole userRole;
    private LocalDate registeredDate;
    private LocalDate updatedDate;
}
