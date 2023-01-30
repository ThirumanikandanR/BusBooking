package com.busbooking.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.admin.request.BusDetailsDto;
import com.busbooking.admin.service.AdminService;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	AdminService adminService;

	@PostMapping("/save/bus/info")
	public ResponseEntity<?> saveBusDetails(@RequestBody BusDetailsDto busDetailsDto) {
		return adminService.saveBusDetails(busDetailsDto);

	}

	@PutMapping("/update/bus/info/{id}")
	public ResponseEntity<?> updateBusDetails(@PathVariable String id, @RequestBody BusDetailsDto updaDto) {
		return adminService.updateBusDetails(id, updaDto);

	}

	@DeleteMapping("/cancel/bus/{busId}")
	public ResponseEntity<?> cancelBus(@PathVariable String busId) {
		return adminService.cancelBus(busId);
	}

	@GetMapping("/get/all/user")
	public ResponseEntity<?> viewAllUsers() {
		return adminService.viewAllUsers();
	}

	@GetMapping("/view/all/passengers/{busId}")
	public ResponseEntity<?> viewAllPassengersByBusId(@PathVariable String busId) {
		return adminService.viewAllPassengersByBusId(busId);

	}

	@PutMapping("/update/seat/count")
	public ResponseEntity<?> updateSeatCount(@RequestParam(value = "busId") String busId,
			@RequestParam(value = "seatCount") int seatCount) {
		return adminService.updateSeatCount(busId, seatCount);

	}

}
