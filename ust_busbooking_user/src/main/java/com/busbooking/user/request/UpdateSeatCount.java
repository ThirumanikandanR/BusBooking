package com.busbooking.user.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSeatCount {

	private String busId;
	
	private int seatCount;
}
