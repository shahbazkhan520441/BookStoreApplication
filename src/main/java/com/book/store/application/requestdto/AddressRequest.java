package com.book.store.application.requestdto;

import com.book.store.application.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddressRequest {

    @NotBlank(message = "Street address cannot be blank")
    @Size(max = 100, message = "Street address must not exceed 100 characters")
    private String streetAddress;

    @Size(max = 100, message = "Street address additional must not exceed 100 characters")
    private String streetAddressAdditional; // Optional field, so no @NotBlank needed

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    @NotNull(message = "Pincode cannot be null")
    @Pattern(regexp = "\\d{5,6}", message = "Pincode must be 5 to 6 digits")
    private int pincode;

    @NotNull(message = "Address type is required")
    private AddressType addressType;
}
