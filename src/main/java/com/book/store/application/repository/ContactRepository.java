package com.book.store.application.repository;

import com.book.store.application.entity.Address;
import com.book.store.application.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {


//    Optional<Contact> findByContactNumber(Long contactNumber);

}