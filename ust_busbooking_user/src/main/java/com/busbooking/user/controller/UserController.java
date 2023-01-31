package com.busbooking.user.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.user.request.BookTicketsDto;
import com.busbooking.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('CUSTOMER')")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/view/all/busDetails")
	public ResponseEntity<?> viewAllBus() {
		return userService.viewAllBus();
	}

	@GetMapping("/view/bus/date/places")
	public ResponseEntity<?> sortBusByDateAndPlaces(
			@RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
			@RequestParam(value = "fromPlace") String fromPlace, @RequestParam(value = "toPlace") String toPlace) {
		return userService.sortBusByDateAndPlaces(date, fromPlace, toPlace);
	}

	@PostMapping("/book/ticket")
	public ResponseEntity<?> bookTickets(@RequestBody BookTicketsDto bookTicketsDto) {

		return userService.bookTickets(bookTicketsDto);
	}
	
	@GetMapping("/view/tickets/customersId/{id}")
	public ResponseEntity<?> viewTicketsByCustomerId(@PathVariable String id){
		return userService.viewTicketsByCustomerId(id);
	}

	@GetMapping("/view/tickets/ticketId/{tId}")
	public ResponseEntity<?> viewTicketsByTicketId(@PathVariable String tId){
		return userService.viewTicketsByTicketId(tId);
	}
	
	
	@GetMapping("/cancel/ticket/{tId}")
	public ResponseEntity<?> cancelTickets(@PathVariable String tId) {

		return userService.cancelTickets(tId);
	}
	
}
