package com.busbooking.auth.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.busbooking.data.enums.ERole;

import lombok.Data;

@Data
public class SignupRequest {

	private String username;

	@NotBlank
	private String password;

	private String firstName;

	private String lastName;

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
