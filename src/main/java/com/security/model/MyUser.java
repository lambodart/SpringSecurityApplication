package com.security.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser { 

	@Id
	//@UuidGenerator
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private Long id;
	private String username;
	private String password;
	private String role; // Eg: ADMIN,USER

}