package com.busbooking.auth.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignupRequest {

	private String username;

	@NotBlank
	private String password;

	private String firstName;

	private String LastName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String mobileNum;

	private String age;

	private String gender;

	private String city;

	private String state;

	private String country;
	
	@NotNull
	private Set<String> role;

}
