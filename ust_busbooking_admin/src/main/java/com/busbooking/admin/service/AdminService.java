package com.busbooking.admin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.admin.request.BusDetailsDto;

@Service
public interface AdminService {

	ResponseEntity<?> saveBusDetails(BusDetailsDto busDetailsDto);

}
