package com.busbooking.auth.serviceimpl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.auth.payload.request.SignupRequest;
import com.busbooking.auth.service.AuthService;
import com.busbooking.data.enums.ERole;
import com.busbooking.data.exception.RoleNotFoundException;
import com.busbooking.data.exception.UserInfoException;
import com.busbooking.data.model.Role;
import com.busbooking.data.model.User;
import com.busbooking.data.payload.response.MessageResponse;
import com.busbooking.data.repository.RoleRepository;
import com.busbooking.data.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private Environment env;
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public ResponseEntity<?> singUpService(@Valid SignupRequest signUpRequest, HttpServletRequest request) {
		
		User user = User.builder().username(signUpRequest.getUsername()).password(signUpRequest.getPassword())
				.firstName(signUpRequest.getFirstName()).LastName(signUpRequest.getLastName())
				.mobileNum(signUpRequest.getMobileNum()).age(signUpRequest.getAge()).gender(signUpRequest.getGender())
				.email(signUpRequest.getEmail()).city(signUpRequest.getCity()).state(signUpRequest.getState())
				.country(signUpRequest.getCountry())
				.build();
		if(signUpRequest.getRole().contains("ROLE_CUSTOMER")) {
			user.setUsername(signUpRequest.getMobileNum()+"");
		}else {	
			user.setUsername(signUpRequest.getEmail());
		}
		if (userRepository.existsByUsername(String.valueOf(signUpRequest.getMobileNum()))) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse(env.getProperty("mobilenumber.exist"), HttpStatus.BAD_REQUEST.value()));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse(env.getProperty("email.exist"), HttpStatus.BAD_REQUEST.value()));
		}
		
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		user.setRoles(getRole(strRoles, roles));
		try {
			user = userRepository.save(user);
		} catch (Exception e) {
			// TODO: handle exception
			throw new UserInfoException();
		}
		
		return ResponseEntity
				.ok(new MessageResponse(HttpStatus.OK.value(), env.getProperty("registered.success"), user));
		
	}
	
	private Set<Role> getRole(Set<String> strRoles, Set<Role> roles) {
		if (Objects.isNull(strRoles)) {
			if (!roleRepository.existsByName(ERole.ROLE_CUSTOMER)) {
				throw new RoleNotFoundException();
			}
			Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER).get();
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ROLE_ADMIN":
					if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
						throw new RoleNotFoundException();
					}
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
					roles.add(adminRole);
					break;
				default:
					if (!roleRepository.existsByName(ERole.ROLE_CUSTOMER)) {
						throw new RoleNotFoundException();
					}
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER).get();
					roles.add(userRole);
				}
			});
		}
		return roles;
	}


}
