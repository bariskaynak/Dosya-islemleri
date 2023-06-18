package com.fileoperations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fileoperations.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
