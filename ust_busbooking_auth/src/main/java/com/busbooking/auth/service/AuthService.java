package com.busbooking.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.auth.payload.request.LoginRequest;
import com.busbooking.auth.payload.request.SignupRequest;

@Service
public interface AuthService {

	ResponseEntity<?> singUpService(@Valid SignupRequest signUpRequest, HttpServletRequest request);

	ResponseEntity<?> authenticationService(@Valid LoginRequest loginRequest);

}
