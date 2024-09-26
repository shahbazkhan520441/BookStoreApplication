package com.book.store.application.service;

import java.util.List;

import com.book.store.application.entity.User;

import com.book.store.application.requestdto.UserAuthRequest;
import com.book.store.application.responsedto.AuthResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.book.store.application.enums.UserRole;
import com.book.store.application.requestdto.MainUserRequest;
import com.book.store.application.requestdto.OtpVerificationRequest;
import com.book.store.application.requestdto.UserRequest;
import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.util.ResponseStructure;

import jakarta.validation.Valid;


public interface UserService {

	String usernameGenerate(String email);

	public void grantRefreshToken(HttpHeaders httpHeaders, User user);

	public void grantAccessToken(HttpHeaders httpHeaders, User user);

	void mailSend(String email, String subject, String text);

	 ResponseEntity<ResponseStructure<UserResponse>> saveUser(MainUserRequest userRequest, UserRole userRole);
	 
	  ResponseEntity<ResponseStructure<UserResponse>> verifyUserOtp(OtpVerificationRequest otpVerificationRequest);

	ResponseEntity<ResponseStructure<UserResponse>> resendOtp(OtpVerificationRequest otpVerificationRequest);

	ResponseEntity<ResponseStructure<UserResponse>> updateUser(MainUserRequest userRequest,  Long userId);

	ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmailVerification( UserRequest userRequest,
			 String otp);

	ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmail(String email);

	ResponseEntity<ResponseStructure<UserResponse>> findUser( Long userId);

	ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers();


    ResponseEntity<ResponseStructure<AuthResponse>> login(UserAuthRequest authRequest);

    ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken);
}
