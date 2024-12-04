package com.book.store.application.controller;

import java.sql.SQLOutput;
import java.util.List;

import com.book.store.application.entity.User;
import com.book.store.application.exception.UserNotExistException;
import com.book.store.application.repository.UserRepository;
import com.book.store.application.requestdto.UserAuthRequest;
import com.book.store.application.responsedto.AuthResponse;
import com.book.store.application.responsedto.LogoutResponse;
import com.book.store.application.responsedto.OtpVerficationResponse;
import com.book.store.application.util.ErrorStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.book.store.application.enums.UserRole;
import com.book.store.application.requestdto.MainUserRequest;
import com.book.store.application.requestdto.OtpVerificationRequest;
import com.book.store.application.requestdto.UserRequest;
import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.UserService;
import com.book.store.application.util.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * This controller handles all user-related operations such as registration, login, OTP verification,
 * password reset, and CRUD operations for users (SELLERS, CUSTOMERS, and ADMINS).
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Users Endpoints", description = "Contains all the endpoints related to the Users entity")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

//    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Test endpoint for checking the user repository.
     *
     * @return success message if the user is found.
     */
    @GetMapping("/test")
    public String test() {
        User user = userRepository.findByUsername("shahbazkhan520441").orElseThrow(() -> new UserNotExistException("USERNAME IS INVALID"));
        System.out.println("in login service impl :" + user.getUsername());
        System.out.println("in login service impl :" + user.getPassword());
        return "success";
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * User login endpoint.
     *
     * @param authRequest contains username and password.
     * @return a response with authentication token.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody UserAuthRequest authRequest) {
        System.out.println(authRequest.getUsername()+" " + authRequest.getPassword());
        return userService.login(authRequest);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Register a new seller in the system.
     *
     * @param userRequest the seller registration request.
     * @return a response with the registered seller details.
     */
    @PostMapping("/sellers/register")
    @Operation(summary = "Register a new seller", description = "Adds a seller to the database.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Seller added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Input",
                            content = @Content(schema = @Schema(implementation = ErrorStructure.class)))
            })
    public ResponseEntity<ResponseStructure<UserResponse>> addSeller(@Valid @RequestBody MainUserRequest userRequest) {
        return userService.saveUser(userRequest, UserRole.SELLER);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Register a new customer in the system.
     *
     * @param userRequest the customer registration request.
     * @return a response with the registered customer details.
     */
    @PostMapping("/customers/register")
    @Operation(summary = "Register a new customer", description = "Adds a customer to the database.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Input",
                            content = @Content(schema = @Schema(implementation = ErrorStructure.class)))
            })
    public ResponseEntity<ResponseStructure<UserResponse>> addCustomer(@Valid @RequestBody MainUserRequest userRequest) {
        return userService.saveUser(userRequest, UserRole.CUSTOMER);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Register a new admin in the system.
     *
     * @param userRequest the admin registration request.
     * @return a response with the registered admin details.
     */
    @PostMapping("/admin/register")
    @Operation(summary = "Register a new admin", description = "Adds an admin to the database.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Admin added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Input",
                            content = @Content(schema = @Schema(implementation = ErrorStructure.class)))
            })
    public ResponseEntity<ResponseStructure<UserResponse>> addAdmin(@Valid @RequestBody MainUserRequest userRequest) {
        return userService.saveUser(userRequest, UserRole.ADMIN);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieve a user by their ID.
     *
     * @param userId the ID of the user.
     * @return the user's details.
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "Find a user by ID", description = "Fetches the user details based on the provided user ID.")
    public ResponseEntity<ResponseStructure<UserResponse>> findUser(@Valid @PathVariable Long userId) {
        return userService.findUser(userId);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * OTP verification for user account activation.
     *
     * @param otpVerificationRequest contains OTP and user details.
     * @return a response confirming OTP verification.
     */
    @PostMapping("/users/otpVerification")
    @Operation(summary = "Verify user OTP", description = "Verifies the OTP to activate the user account.")
    public ResponseEntity<ResponseStructure<OtpVerficationResponse>> verifyUser(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        return userService.verifyUserOtp(otpVerificationRequest);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Resend OTP to the user.
     *
     * @param otpVerificationRequest contains the user details for OTP.
     * @return a response confirming OTP resending.
     */
    @PostMapping("/users/resendOtp")
    @Operation(summary = "Resend OTP", description = "Resends OTP for account verification.")
    public ResponseEntity<ResponseStructure<UserResponse>> resendOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        return userService.resendOtp(otpVerificationRequest);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Update a user's details by their ID.
     *
     * @param userRequest the updated user details.
     * @param userId      the ID of the user to update.
     * @return the updated user's details.
     */
    @PutMapping("/users/{userId}")
    @Operation(summary = "Update user details", description = "Updates a user's details based on their user ID.")
    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(@Valid @RequestBody MainUserRequest userRequest, @Valid @PathVariable Long userId) {
        return userService.updateUser(userRequest, userId);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Reset the user's password using email.
     *
     * @param email the email address to send the password reset link.
     * @return a response confirming the password reset process.
     */
    @PutMapping("/users/update/{email}")
    @Operation(summary = "Password reset by email", description = "Resets the user's password using email.")
    public ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmail(@Valid @PathVariable String email) {
        return userService.passwordResetByEmail(email);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Reset the user's password using OTP verification.
     *
     * @param userRequest the updated user details.
     * @param otp         the OTP used for verification.
     * @return a response confirming the password reset.
     */
    @PutMapping("/users/update")
    @Operation(summary = "Password reset by OTP", description = "Resets the user's password after OTP verification.")
    public ResponseEntity<ResponseStructure<UserResponse>> passwordResetByEmailVerification(@Valid @RequestPart("userRequest") UserRequest userRequest, @Valid @RequestParam("otp") String otp) {
        return userService.passwordResetByEmailVerification(userRequest, otp);
    }

    //    ----------------------------------------------------------------------0-------------------------------------------------------------------------

    /**
     * Retrieve all users from the database.
     *
     * @return a list of users (SELLER, CUSTOMER, ADMIN).
     */
    @GetMapping("/users")
    @Operation(summary = "Retrieve all users", description = "Fetches all users from the database.")
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers() {
        return userService.findUsers();
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Refresh the login session using a refresh token.
     *
     * @param refreshToken the refresh token from cookies.
     * @return a response with a new access token.
     */
    @PostMapping("/refreshLogin")
    @Operation(summary = "Refresh login session", description = "Generates a new access token using the refresh token.")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(value = "rt", required = false) String refreshToken) {
        return userService.refreshLogin(refreshToken);
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Logout the user and invalidate the access and refresh tokens.
     *
     * @param refreshToken the refresh token from cookies.
     * @param accessToken  the access token from cookies.
     * @return a response confirming the logout.
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout the user", description = "Logs out the user by invalidating the access and refresh tokens.")
    public ResponseEntity<LogoutResponse> logout(@CookieValue(value = "rt", required = false) String refreshToken, @CookieValue(value = "at", required = false) String accessToken) {
        System.out.println(" in logou of controller ");
        return userService.logout(refreshToken, accessToken);
    }
//    -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
