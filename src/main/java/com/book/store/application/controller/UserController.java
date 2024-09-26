package com.book.store.application.controller;

import java.util.List;

import com.book.store.application.entity.User;
import com.book.store.application.exception.UserNotExistException;
import com.book.store.application.repository.UserRepository;

import com.book.store.application.requestdto.UserAuthRequest;
import com.book.store.application.responsedto.AuthResponse;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import com.book.store.application.enums.UserRole;
import com.book.store.application.requestdto.MainUserRequest;
import com.book.store.application.requestdto.OtpVerificationRequest;
import com.book.store.application.requestdto.UserRequest;
import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.UserService;
import com.book.store.application.util.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/test")
    public String test(){
        User user = userRepository.findByUsername("shahbazkhan520441").orElseThrow(()-> new UserNotExistException(" USERNAME IS INVALID "));
        System.out.println("in login service impl :"+ user.getUsername());
        System.out.println("in login service impl :"+ user.getPassword());
        return "success";
    }
    
    
    //------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(
            @RequestBody UserAuthRequest authRequest) {
        return userService.login(authRequest);
    }

    //------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/sellers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addSeller(@Valid @RequestBody MainUserRequest userRequest) {
    	System.out.println("in seller");
    	return userService.saveUser(userRequest, UserRole.SELLER);
    }

    //------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/customers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addCustomer(@Valid @RequestBody MainUserRequest userRequest) {
        return userService.saveUser(userRequest, UserRole.CUSTOMER);
    }
    
    @PostMapping("/admin/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addAdmin(@Valid @RequestBody MainUserRequest userRequest) {
        return userService.saveUser(userRequest, UserRole.ADMIN);
    }
    
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseStructure<UserResponse>> findUser(
            @Valid @PathVariable Long userId) {
        return userService.findUser(userId);
    }


    @PostMapping("/users/otpVerification")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUser(
    		 @RequestBody OtpVerificationRequest otpVerificationRequest) {
    	System.out.println("in controller");
    	System.out.println("in controller");
        return userService.verifyUserOtp(otpVerificationRequest);
    }
    
    @PostMapping("/users/resendOtp")
    public ResponseEntity<ResponseStructure<UserResponse>> resendOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        return userService.resendOtp(otpVerificationRequest);
    }


    //------------------------------------------------------------------------------------------------------------------------
  
    @PutMapping("/users/{userId}")
    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(
            @Valid @RequestBody MainUserRequest userRequest,
            @Valid @PathVariable Long userId) {
        return userService.updateUser(userRequest, userId);
    }

    //------------------------------------------------------------------------------------------------------------------------
    
    @PutMapping("/users/update/{email}")
    public ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmail(
            @Valid  @PathVariable String email) {
        return userService.passwordResetByEmail(email);
    }
    
    @PutMapping("/users/update")
    public ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmailVerification(
            @Valid  @RequestBody UserRequest userRequest,
            @Valid @RequestParam String otp) {
        return userService.passwordResetByEmailVerification(userRequest, otp);
    }
    
    
    //------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/users")
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers() {
        return userService.findUsers();
    }

    //------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/refreshLogin")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(
            @CookieValue(value = "rt", required = false) String refreshToken) {
        return userService.refreshLogin(refreshToken);
    }



    
    
    

}