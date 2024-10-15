package com.book.store.application.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
	
	@NotBlank(message= "Email can not be blank")
	@NotNull(message ="Email can not be null")
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "invalid email ")
	private String email;

	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password must"
			+ " contain at least one Capital letter, one small letter, one number, one special character")
	@NotBlank(message = "Password can not be blank")
	@NotBlank(message = "Password can not be blank")
	private String password;
	
	

}
