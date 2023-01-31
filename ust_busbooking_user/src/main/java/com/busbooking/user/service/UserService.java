package com.busbooking.user.service;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.user.request.BookTicketsDto;

@Service
public interface UserService {

	ResponseEntity<?> viewAllBus();

	ResponseEntity<?> sortBusByDateAndPlaces(LocalDate date, String fromPlace, String toPlace);

	ResponseEntity<?> bookTickets(BookTicketsDto bookTicketsDto);

	ResponseEntity<?> viewTicketsByCustomerId(String id);

	ResponseEntity<?> viewTicketsByTicketId(String tId);

	ResponseEntity<?> cancelTickets(String tId);

}
