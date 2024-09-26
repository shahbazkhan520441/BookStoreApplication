package com.book.store.application.repository;

import java.util.List;
import java.util.Optional;

import com.google.common.io.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.book.store.application.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);


}
