package com.busbooking.admin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.admin.request.BusDetailsDto;

@Service
public interface AdminService {

	ResponseEntity<?> saveBusDetails(BusDetailsDto busDetailsDto);

	ResponseEntity<?> updateBusDetails(String id, BusDetailsDto updaDto);

	ResponseEntity<?> cancelBus(String busId);

	ResponseEntity<?> viewAllUsers();

	ResponseEntity<?> viewAllPassengersByBusId(String busId);

	ResponseEntity<?> updateSeatCount(String busId, int seatCount);

	

}
