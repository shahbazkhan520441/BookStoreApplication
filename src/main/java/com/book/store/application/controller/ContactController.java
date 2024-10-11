package com.book.store.application.controller;

import com.book.store.application.entity.Contact;
import com.book.store.application.service.ContactService;
import com.book.store.application.util.ErrorStructure;
import com.book.store.application.util.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Contact Endpoints", description = "Contains all the endpoints related to contact management for addresses in the bookstore application")
public class ContactController {
    private final ContactService contactService;

    //-----------------------------------------------------------------------------------------------

    /**
     * Adds a new contact associated with a specific address.
     *
     * @param contact    the Contact object containing the details of the contact to be added.
     * @param addressId  the ID of the address to which the contact belongs.
     * @return ResponseEntity containing a ResponseStructure with the added Contact object.
     */
    @PostMapping("/addresses/{addressId}/contacts")
    @Operation(summary = "Add Contact", description = "Adds a new contact associated with the specified address.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Contact successfully added",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<Contact>> addContact(
            @RequestBody Contact contact,
            @PathVariable Long addressId) {
        return contactService.addContact(contact, addressId);
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Updates an existing contact associated with a specific address.
     *
     * @param addressId  the ID of the address to which the contact belongs.
     * @param contactId  the ID of the contact to be updated.
     * @param contact    the Contact object containing the updated details of the contact.
     * @return ResponseEntity containing a ResponseStructure with the updated Contact object.
     */
    @PutMapping("/addresses/{addressId}/contacts/{contactId}")
    @Operation(summary = "Update Contact", description = "Updates an existing contact associated with the specified address.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contact successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Contact or address not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<Contact>> updateContact(
            @PathVariable Long addressId,
            @PathVariable Long contactId,
            @RequestBody Contact contact) {
        return contactService.updateContact(addressId, contactId, contact);
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Retrieves a list of contacts associated with a specific address.
     *
     * @param addressId  the ID of the address for which contacts are to be retrieved.
     * @return ResponseEntity containing a ResponseStructure with a list of Contact objects.
     */
    @GetMapping("/addresses/{addressId}/contacts")
    @Operation(summary = "Get Contacts", description = "Retrieves a list of contacts associated with the specified address.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contacts successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "404", description = "No contacts found for the given address", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<Contact>>> getContacts(
            @PathVariable Long addressId) {
        return contactService.getContacts(addressId);
    }

    //-----------------------------------------------------------------------------------------------
}
