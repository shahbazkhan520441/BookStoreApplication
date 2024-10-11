package com.book.store.application.controller;

import com.book.store.application.responsedto.UserResponse;
import com.book.store.application.service.CustomerService;
import com.book.store.application.util.ErrorStructure;
import com.book.store.application.util.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Customer Endpoints", description = "Contains all the endpoints related to customer management in the bookstore application")
public class CustomerController {

    private final CustomerService customerService;

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves the details of a specific customer by their ID.
     *
     * @param customerId the ID of the customer to retrieve.
     * @return ResponseEntity containing a ResponseStructure with the UserResponse object for the specified customer.
     */
    @GetMapping("/customers/{customerId}")
    @Operation(summary = "Find Customer", description = "Retrieves the details of a specific customer by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<UserResponse>> findCustomer(@Valid @PathVariable Long customerId) {
        return customerService.findCustomer(customerId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a list of all customers in the system.
     *
     * @return ResponseEntity containing a ResponseStructure with a list of UserResponse objects for all customers.
     */
    @GetMapping("/customers")
    @Operation(summary = "Find Customers", description = "Retrieves a list of all customers in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customers successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "404", description = "No customers found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers() {
        return customerService.findCustomers();
    }

    //------------------------------------------------------------------------------------------------------------------------
}
