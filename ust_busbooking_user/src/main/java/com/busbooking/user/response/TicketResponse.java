package com.busbooking.user.response;

import java.util.List;

import com.busbooking.data.model.BusDetails;
import com.busbooking.data.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponse {
	
	private UserDetails userDetails;
	
	private BusDetails busDetails;
	
	private List<PassengerResponse> passengers;

}

