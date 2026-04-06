package com.exolon.finance.finance_tracker.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	
	@Size(min = 2)
	@NotBlank(message = "First name is required")
	@Column(nullable = false)
	private String firstName;
	
	private String lastName;
	
	@Column(unique = true, nullable = false)
	@Email(message = "Email must be valid")
	@NotBlank(message = "Email is required")
	private String email;
	
	
	@NotBlank(message = "Password is required")
	@Column(nullable = false)
	@JsonIgnore 
	private String password;
	
	@Column(nullable = false, length = 2)
	private String country; 
	
	@Column(nullable = false, length = 3)
	private String currency; 
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Role is required")
	private Role role;
	
	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;
    
}
