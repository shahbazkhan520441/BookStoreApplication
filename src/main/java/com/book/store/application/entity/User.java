package com.book.store.application.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.book.store.application.enums.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;

	private String firstName;
	private String lastName;
	 private String username;
	private LocalDate dob;
	private LocalDate registeredDate;
	private LocalDate updatedDate;
	private String email;
    private String password;
    @Enumerated(EnumType.STRING)
	private UserRole userRole;
    private boolean isEmailVerified;
    private boolean isDeleted;

}
