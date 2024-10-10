package com.book.store.application.service;

import com.book.store.application.entity.Contact;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface ContactService {

    ResponseEntity<ResponseStructure<Contact>> addContact(Contact contact, Long addressId);

    ResponseEntity<ResponseStructure<Contact>> updateContact(Long addressId , Long contactId, Contact contact);

    ResponseEntity<ResponseStructure<List<Contact>>> getContacts(Long addressId);
}