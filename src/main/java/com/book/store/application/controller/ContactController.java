package com.book.store.application.controller;

import com.book.store.application.entity.Contact;
import com.book.store.application.service.ContactService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ContactController {
    private final ContactService contactService;
    //-----------------------------------------------------------------------------------------------

    @PostMapping("/addresses/{addressId}/contacts")
    public ResponseEntity<ResponseStructure<Contact>> addContact(
            @RequestBody Contact contact,
            @PathVariable Long addressId) {
        return contactService.addContact(contact, addressId);
    }
    //-----------------------------------------------------------------------------------------------

    @PutMapping("/addresses/{addressId}/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<Contact>> updateContact(
            @PathVariable Long addressId,
            @PathVariable Long contactId,
            @RequestBody Contact contact) {
        return contactService.updateContact(addressId, contactId, contact);
    }
    //-----------------------------------------------------------------------------------------------

    @GetMapping("/addresses/{addressId}/contacts")
    public ResponseEntity<ResponseStructure<List<Contact>>> getContacts(
            @PathVariable Long addressId) {
        return contactService.getContacts(addressId);
    }
    //-----------------------------------------------------------------------------------------------

}