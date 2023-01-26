package com.busbooking.admin.response;

import java.util.List;

import com.busbooking.data.model.BusDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllPassengerResponse {
	
	private BusDetails bus;
	
	private List<PassengerDetails> passengers;

}
