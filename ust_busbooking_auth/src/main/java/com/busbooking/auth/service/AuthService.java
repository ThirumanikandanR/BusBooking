package com.busbooking.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.auth.payload.request.SignupRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Service
public interface AuthService {

	ResponseEntity<?> singUpService(@Valid SignupRequest signUpRequest, HttpServletRequest request);

}
