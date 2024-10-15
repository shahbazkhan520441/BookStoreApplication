package com.book.store.application.serviceimpl;
import com.book.store.application.entity.Address;
import com.book.store.application.entity.Contact;
import com.book.store.application.exception.AddressNotExistException;
import com.book.store.application.exception.ContactAlReadyExistException;
import com.book.store.application.exception.ContactNotExistException;
import com.book.store.application.exception.IllegalOperationException;
import com.book.store.application.repository.AddressRepository;
import com.book.store.application.repository.ContactRepository;
import com.book.store.application.service.ContactService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    //-----------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<Contact>> addContact(Contact contact, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotExistException("AddressId " + addressId + ", not exist"));
        if ((contact.getContactNumber() + "").length() != 10) {
            throw new IllegalOperationException("Mobile Number must be 10 digits");
        }
        if (address.getContacts() == null) {
            contact.setAddress(address);
            contact = contactRepository.save(contact);
            address.setContacts(List.of(contact));
        } else {
            List<Contact> contacts = address.getContacts();
            if (contacts.size() >= 2) {
                throw new ContactAlReadyExistException("Only 2 contacts are allowed");
            }
            for (Contact contact1 : contacts) {
                if (contact1.getContactNumber().equals(contact.getContactNumber())) {
                    throw new ContactAlReadyExistException(contact.getContactNumber() + ", is all ready exist");
                } else if (contact1.getPriority().equals(contact.getPriority())) {
                    throw new IllegalOperationException("Contact : " + contact.getPriority() + ", already exist");
                }
            }
            contact.setAddress(address);
            contact = contactRepository.save(contact);
            contacts.add(contact);
            address.setContacts(contacts);
        }
        addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<Contact>()
                .setStatus(HttpStatus.CREATED.value())
                .setMessage("Contact Created")
                .setData(contact));
    }
    //-----------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<Contact>> updateContact(
            Long addressId,
            Long contactId,
            Contact contact) {
        if (String.valueOf(contact.getContactNumber()).length() != 10) {
            throw new IllegalOperationException("Mobile Number must be 10 digits");
        }
        Contact contactToUpdate = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotExistException("ContactId: " + contactId + " does not exist"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotExistException("AddressId: " + addressId + " does not exist"));

        for (Contact existingContact : address.getContacts()) {
            if (!existingContact.getContactId().equals(contactId)) {
                if (existingContact.getContactNumber().equals(contact.getContactNumber())) {
                    throw new ContactAlReadyExistException("Contact number " + contact.getContactNumber() + " already exists");
                }
                if (existingContact.getPriority().equals(contact.getPriority())) {
                    throw new IllegalOperationException("Priority " + contact.getPriority() + " is already assigned to another contact");
                }
            }
        }
        // Proceed with updating the contact details
        contactToUpdate.setContactNumber(contact.getContactNumber());
        contactToUpdate.setPriority(contact.getPriority());
        contactToUpdate = contactRepository.save(contactToUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<Contact>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Contact Updated")
                .setData(contactToUpdate));
    }
    //-----------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> getContacts(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotExistException("AddressId " + addressId + ", not exist"));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<Contact>>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Contact Founded")
                .setData(address.getContacts()));
    }
    //-----------------------------------------------------------------------------------------------

}