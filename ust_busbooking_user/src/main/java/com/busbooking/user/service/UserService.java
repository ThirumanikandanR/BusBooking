package com.busbooking.user.service;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

	ResponseEntity<?> viewAllBus();

	ResponseEntity<?> sortBusByDateAndPlaces(LocalDate date, String fromPlace, String toPlace);

}
