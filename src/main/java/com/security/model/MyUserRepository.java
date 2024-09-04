package com.security.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, String> { 

    Optional<MyUser> findByUsername(String username);
}