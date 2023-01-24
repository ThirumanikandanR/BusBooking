package com.busbooking.admin.serviceImpl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.busbooking.admin.request.BusDetailsDto;
import com.busbooking.admin.service.AdminService;
import com.busbooking.data.model.BusDetails;
import com.busbooking.data.payload.response.MessageResponse;
import com.busbooking.data.repository.BusDetailsRepository;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	BusDetailsRepository busdeRepository;
	
	@Autowired
	Environment env;

	@Override
	public ResponseEntity<?> saveBusDetails(BusDetailsDto busDetailsDto) {
		if(Objects.isNull(busDetailsDto)) {
			return ResponseEntity.ok(
					new MessageResponse(env.getProperty("invalid.input"), HttpStatus.BAD_REQUEST.value()));
		}
		BusDetails busDetails=null;
		
		try {
			busDetails = BusDetails.builder()
					.busNo(busDetailsDto.getBusNo())
					.busName(busDetailsDto.getBusName())
					.driverName(busDetailsDto.getDriverName())
					.contNum(busDetailsDto.getContNum())
					.noOfSeats(busDetailsDto.getNoOfSeats())
					.date(busDetailsDto.getDate())
					.depTime(busDetailsDto.getDepTime())
					.arvTime(busDetailsDto.getArvTime())
					.tkkPrice(busDetailsDto.getTkkPrice())
					.build();
			busdeRepository.save(busDetails);
			
			return ResponseEntity.ok(new MessageResponse(HttpStatus.OK.value(),
					env.getProperty("busDetails.saved"), busDetails));
		}
		catch(Exception e) {
			return ResponseEntity.ok(
					new MessageResponse(env.getProperty("BusDetails.not.saved"), HttpStatus.BAD_REQUEST.value()));
		}
	}
	

}
