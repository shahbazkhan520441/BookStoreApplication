package com.book.store.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.book.store.application.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);


}
