package com.busbooking.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.admin.request.BusDetailsDto;
import com.busbooking.admin.service.AdminService;

import io.swagger.models.Response;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	
	@PostMapping("/save/bus/info")
	public ResponseEntity<?> saveBusDetails(@RequestBody BusDetailsDto busDetailsDto){
		return adminService.saveBusDetails(busDetailsDto);
		
	}
	
	
}
