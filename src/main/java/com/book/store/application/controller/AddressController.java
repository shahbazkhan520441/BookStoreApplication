package com.book.store.application.controller;

import com.book.store.application.requestdto.AddressRequest;
import com.book.store.application.responsedto.AddressResponse;
import com.book.store.application.service.AddressService;
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
import org.springframework.validation.annotation.Validated; // Import this
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Address Endpoints", description = "Contains all the endpoints related to managing user addresses in the bookstore application")
@Validated // Add this to ensure validation is applied
public class AddressController {

    private final AddressService addressService;

    //------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/users/{userId}/addresses")
    @Operation(description = "The endpoint is used to add a new address for a specific user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Address successfully added"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "User not found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(
            @Valid @RequestBody AddressRequest addressRequest, // Add @Valid here
            @PathVariable Long userId) {
        return addressService.addAddress(addressRequest, userId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/users/{userId}/addresses")
    @Operation(description = "The endpoint is used to retrieve all addresses of a specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Addresses successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "User or addresses not found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<AddressResponse>>> getAddress(@PathVariable Long userId) {
        return addressService.getAddress(userId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    @PutMapping("/users/addresses/{addressId}")
    @Operation(description = "The endpoint is used to update an existing address by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Address not found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest addressRequest) {
        return addressService.updateAddress(addressId, addressRequest);
    }

}
