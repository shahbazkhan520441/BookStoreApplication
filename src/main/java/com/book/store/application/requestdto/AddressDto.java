package com.book.store.application.requestdto;

import com.book.store.application.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

    @NotNull(message = "Address type cannot be null")
    private AddressType addressType;

    @NotBlank(message = "Street address cannot be blank")
    @Size(max = 100, message = "Street address must not exceed 100 characters")
    private String streetAddress;

    private String streetAddressAdditional; // Optional, no validation needed unless you want to impose length limits

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

    @NotBlank(message = "Primary contact number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Primary contact number must be 10 digits")
    private String contactNumber1;

    @Pattern(regexp = "\\d{10}", message = "Secondary contact number must be 10 digits")
    private String contactNumber2; // Optional field, so no @NotBlank
}
