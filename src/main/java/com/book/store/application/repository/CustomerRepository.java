package com.book.store.application.repository;

import com.book.store.application.entity.Address;
import com.book.store.application.entity.Contact;
import com.book.store.application.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    Set<Contact> findByAddress(Address address);
}