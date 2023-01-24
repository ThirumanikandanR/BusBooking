package com.busbooking.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('CUSTOMER')")
public class UserController {

	
	@GetMapping("/get")
	public String test() {
		return "user working success!!";
	}
}
